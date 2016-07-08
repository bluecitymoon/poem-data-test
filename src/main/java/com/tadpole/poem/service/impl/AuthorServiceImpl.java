package com.tadpole.poem.service.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tadpole.poem.domain.*;
import com.tadpole.poem.domain.Author;
import com.tadpole.poem.domain.Poem;
import com.tadpole.poem.json.*;
import com.tadpole.poem.repository.ConfigurationRepository;
import com.tadpole.poem.repository.JobLogRepository;
import com.tadpole.poem.repository.PoemRepository;
import com.tadpole.poem.service.AuthorService;
import com.tadpole.poem.repository.AuthorRepository;
import com.tadpole.poem.service.util.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.tadpole.poem.service.util.PrintUtil.println;

/**
 * Service Implementation for managing Author.
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    private final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Inject
    private AuthorRepository authorRepository;

    @Inject
    private ConfigurationRepository configurationRepository;

    @Inject
    private PoemRepository poemRepository;

    @Inject
    private JobLogRepository jobLogRepository;


    /**
     * Save a author.
     *
     * @param author the entity to save
     * @return the persisted entity
     */
    public Author save(Author author) {
        log.debug("Request to save Author : {}", author);
        Author result = authorRepository.save(author);
        return result;
    }

    /**
     * Get all the authors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Author> findAll(Pageable pageable) {
        log.debug("Request to get all Authors");
        Page<Author> result = authorRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one author by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Author findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        Author author = authorRepository.findOne(id);
        return author;
    }

    /**
     * Delete the  author by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        authorRepository.delete(id);
    }

    @Override
    public boolean fillUpAuthorInformation(Job job) {

        List<Author> authors = Lists.newArrayList();

        String authorForceRegrab = configurationRepository.findByIdentifier("AUTHOR-FORCE-REGRAB").getContent();

        if (StringUtils.isNotEmpty(authorForceRegrab) && authorForceRegrab.equals("1")) {
            authors = authorRepository.findAll();
        } else {
            authors = authorRepository.findByDescriptionIsNull();
        }

        WebClient webClient = GrabPageProcessor.newWebClient();

        for (Author author : authors) {
            Author result = fillUpSingleAuthor(author, webClient);

            if (result == null) {
                System.err.println("Grab fail " + author.toString());
            }
        }

        return false;
    }

    @Override
    public Author fillUpSingleAuthor(Author author, WebClient webClient) {

        int retry = 0;

        String fullUrl = configurationRepository.findByIdentifier("SEARCH_BASE").getContent() + author.getLink();
        HtmlPage htmlPage = null;
        try {

            System.err.println(fullUrl);

            htmlPage = webClient.getPage(new URL(fullUrl));

        } catch (Exception e) {

            System.err.println(e.getMessage());

            retry++;

            if (retry < Integer.valueOf(configurationRepository.findByIdentifier("FILL-AUTHOR-RETRY").getContent())) {
                fillUpSingleAuthor(author, webClient);
            } else {
                return null;
            }
        }

        String authorDescriptionXpath = "//*[contains(concat(\" \", normalize-space(@class), \" \"), \" son2 \")]";
        List<HtmlDivision> divisions = (List<HtmlDivision>) htmlPage.getByXPath(authorDescriptionXpath);

        if (divisions == null) {

            return null;
        }

        HtmlDivision descriptionDiv = null;
        if (divisions.size() == 2) {

            descriptionDiv = divisions.get(1);
        } else {
            return null;
        }
        author.setDescription(descriptionDiv.getTextContent().trim());

        List<HtmlImage> images = (List<HtmlImage>) htmlPage.getByXPath(authorDescriptionXpath + "//img");

        if (images != null && !images.isEmpty()) {

            HtmlImage image = images.get(0);
            String src = image.getSrcAttribute();
            List<String> patheElements = Splitter.on("/").splitToList(src);

            String fileName = patheElements.get(patheElements.size() - 1);

            String realFileName = System.currentTimeMillis() + "-" + fileName;
            String fullFilePath = configurationRepository.findByIdentifier("AUTHOR-AVATAR-BATH").getContent() + fileName;

            try {
                image.saveAs(new File(fullFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            author.setAvatarFileName(realFileName);
            author.setReferenceAvatar(src);
        }

        Integer visitCount = author.getVisitCount();
        if (visitCount == null) {
            author.setVisitCount(1);
        } else {
            author.setVisitCount(visitCount + 1);
        }

        Author updatedAuthor = save(author);

        return updatedAuthor;
    }

    @Override
    public void downloadAvatars(Job job, WebClient webClient) {

        for (Author author : authorRepository.findAll()) {

            String fullUrl = configurationRepository.findByIdentifier("SEARCH_BASE").getContent() + author.getLink();

            HtmlPage htmlPage = null;
            try {

                System.err.println(fullUrl);

                htmlPage = webClient.getPage(new URL(fullUrl));

            } catch (Exception e) {

                System.err.println(e.getMessage());

            }

            String authorDescriptionXpath = "//*[contains(concat(\" \", normalize-space(@class), \" \"), \" son2 \")]";

            List<HtmlImage> images = (List<HtmlImage>) htmlPage.getByXPath(authorDescriptionXpath + "//img");

            if (images != null && !images.isEmpty()) {

                HtmlImage image = images.get(0);
                String src = image.getSrcAttribute();
                List<String> patheElements = Splitter.on("/").splitToList(src);

                String fileName = patheElements.get(patheElements.size() - 1);

                String realFileName = System.currentTimeMillis() + "-" + fileName;
                String fullFilePath = configurationRepository.findByIdentifier("AUTHOR-AVATAR-BATH").getContent() + realFileName;

                try {
                    image.saveAs(new File(fullFilePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                author.setAvatarFileName(realFileName);
            }

            Integer visitCount = author.getVisitCount();
            if (visitCount == null) {
                author.setVisitCount(1);
            } else {
                author.setVisitCount(visitCount + 1);
            }

            save(author);
        }
    }

    @Override
    public void objectsToJsonFiles(Job job) {

        ObjectMapper objectMapper = new ObjectMapper();
        List<Poem> poems = poemRepository.findAll();

        List<com.tadpole.poem.json.Poem> jsonPoems = Lists.newArrayList();
        for (Poem poem : poems) {

            String originalContent = new String(poem.getContent());
            String content = poem.getContent().replaceAll("\\t|\\r|\\n|\\s+|\"|“|”", "");
            content = PinyinTranslator.removeGuahaoThingsInString(content);

            poem.setContent(content);

            if (!originalContent.equals(content)) {
                poemRepository.save(poem);
            }

            String regex = "，|。|？|、|！|,|;|；|!|\\?";

            String[] contents = content.split(regex);

            try {

                com.tadpole.poem.json.Poem jsonPoem = com.tadpole.poem.json.Poem.builder()
                    .id(poem.getId())
                    .title(poem.getTitle())
                    .content(contents)
                    .authorId(poem.getAuthor() == null ? null : poem.getAuthor().getId())
                   // .avatar(poem.getAuthor() == null ? null : poem.getAuthor().getAvatarFileName() == null ? null : poem.getAuthor().getAvatarFileName())
                    //.pinyin(poem.getTitlePinyin())
                    .build();

                jsonPoems.add(jsonPoem);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String stringFilesPath = configurationRepository.findByIdentifier("JSON_FILE_PATH").getContent();
        try {
            objectMapper.writeValue(new File(stringFilesPath + "poem.json"), jsonPoems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void authorsToJsonFile(Job job) {

        ObjectMapper objectMapper = new ObjectMapper();
        List<Author> authors = authorRepository.findAll();

        List<com.tadpole.poem.json.Author> jsonAuthors = Lists.newArrayList();
        for (Author author : authors) {

            String description = author.getDescription().replaceAll("　+", "");
            if (description.startsWith(author.getName())) {

                int firstCommaIndex  = description.indexOf("，");

                description = description.substring(firstCommaIndex + 1);
            }
            com.tadpole.poem.json.Author jsonAuthor = com.tadpole.poem.json.Author.builder()
                .id(author.getId())
                .age(author.getAge())
                .avatar(author.getAvatarFileName())
                .birth(StringUtils.isEmpty(author.getBirthYear()) ? null : Integer.valueOf(author.getBirthYear()))
                .death(StringUtils.isEmpty(author.getDieYear()) ? null : Integer.valueOf(author.getDieYear()))
                .name(author.getName())
                .desc(description.split("。|；"))
                .period(author.getPeriod())
                .zi(author.getZi())
                .hao(author.getHao())
                .build();

            jsonAuthors.add(jsonAuthor);
        }

        String stringFilesPath = configurationRepository.findByIdentifier("JSON_FILE_PATH").getContent();
        try {
            objectMapper.writeValue(new File(stringFilesPath + "author.json"), jsonAuthors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseBirthDeathYear(Job job) {

        List<Author> authors = authorRepository.findByBirthYearIsNullAndDieYearIsNull();
        for (Author author : authors) {

            String description = author.getDescription();

            String originalBirthday = AuthorUtil.getPossibleBirthdayString(description);

            if (originalBirthday != null) {

                List<Integer> numbers = MathUtil.getNumbers(originalBirthday);

                if (numbers.size() == 2) {

                    author.setBirthYear("" + numbers.get(0));
                    author.setDieYear("" + numbers.get(1));

                    authorRepository.save(author);

                } else if (numbers.size() == 4) {

                    Integer first = numbers.get(0);
                    Integer second = numbers.get(1);
                    Integer third = numbers.get(2);
                    Integer fourth = numbers.get(3);

                    if (second > first && fourth > third) {

                        author.setBirthYear("" + first);
                        author.setDieYear("" + second);

                        authorRepository.save(author);
                    }

                } else {

                    if (originalBirthday.contains("生卒年不详")) {
                        originalBirthday = "生卒年不详";
                    }

                    if (originalBirthday.length() < 40) {
                        author.setAgeDescription(originalBirthday);
                    }

                    authorRepository.save(author);
                }
            }

            calculateAge(job);

        }

    }

    @Override
    public void grabAuthorLinks(Job job) {

        String baseHref = job.getTarget();

        int page = 1;
        while (true) {
            String fullUrl = baseHref + page;

            try {
                Document document = Jsoup.connect(fullUrl).get();

                Elements elements = document.getElementsByClass("sonsauthor");

                if (elements.size() == 0) {
                    break;
                }

                for (Element element : elements) {

                    Elements links = element.getElementsByTag("a");

                    if (links.size() == 3) {

                        Element imgElement = links.first();
                        String href = imgElement.attr("href");

                        Author author = authorRepository.findByLink(href);

                        if (author == null) {

                            author = new Author();
                            author.setLink(href);

                            Element image = imgElement.children().first();

                            author.setReferenceAvatar(image.attr("src"));
                            author.setName(image.attr("alt"));


                            Elements periodElements = element.getElementsMatchingOwnText(Pattern.compile("朝代：(.*?)"));
                            if (!periodElements.isEmpty()) {

                                String period = periodElements.first().text().substring("朝代：".length());

                                PrintUtil.println(period);
                                author.setPeriod(period);
                            }

                            authorRepository.save(author);

                            JobLog jobLog = new JobLog();
                            jobLog.setMessage("saved new author " + author.toString());
                            jobLog.setJob(job);

                            jobLogRepository.save(jobLog);

                        } else if (author.getPeriod() == null) {

                            Elements periodElements = element.getElementsMatchingOwnText(Pattern.compile("朝代：(.*?)"));
                            if (!periodElements.isEmpty()) {
                                author.setPeriod(periodElements.first().text().substring("朝代：".length()));
                            }

                            authorRepository.save(author);

                            JobLog jobLog = new JobLog();
                            jobLog.setMessage("update period for " + author.getName());
                            jobLog.setJob(job);

                            jobLogRepository.save(jobLog);

                        }

                    } else if (links.size() == 2) {

                        Element nameElement = links.first();
                        String href = nameElement.attr("href");

                        Author author = authorRepository.findByLink(href);

                        if (author == null) {

                            author = new Author();
                            author.setLink(href);
                            author.setName(nameElement.text());


                            Elements periodElements = element.getElementsMatchingOwnText(Pattern.compile("朝代：(.*?)"));
                            if (!periodElements.isEmpty()) {
                                author.setPeriod(periodElements.first().text().substring("朝代：".length()));
                            }

                            authorRepository.save(author);

                            JobLog jobLog = new JobLog();
                            jobLog.setMessage("saved new author " + author.toString());
                            jobLog.setJob(job);

                            jobLogRepository.save(jobLog);

                        } else if (author.getPeriod() == null) {

                            Elements periodElements = element.getElementsMatchingOwnText(Pattern.compile("朝代：(.*?)"));
                            if (!periodElements.isEmpty()) {
                                author.setPeriod(periodElements.first().text().substring("朝代：".length()));
                            }

                            authorRepository.save(author);

                            JobLog jobLog = new JobLog();
                            jobLog.setMessage("update period for " + author.getName());
                            jobLog.setJob(job);

                            jobLogRepository.save(jobLog);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            page++;
        }
    }

    private void calculateAge(Job job) {

        List<Author> authors = authorRepository.findByBirthYearIsNotNullAndDieYearIsNotNullAndAgeIsNull();

        for (Author author : authors) {

            Integer age = Math.abs(Integer.valueOf(author.getDieYear()) - Integer.valueOf(author.getBirthYear()));
            author.setAge(age);

            if (age < 15) {
                author.setBirthYear(null);
                author.setDieYear(null);
                author.setAge(null);
            }

            authorRepository.save(author);
        }
    }
}
