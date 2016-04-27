package com.tadpole.poem.service.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tadpole.poem.domain.Configuration;
import com.tadpole.poem.domain.Job;
import com.tadpole.poem.domain.Poem;
import com.tadpole.poem.repository.ConfigurationRepository;
import com.tadpole.poem.repository.PoemRepository;
import com.tadpole.poem.service.AuthorService;
import com.tadpole.poem.domain.Author;
import com.tadpole.poem.repository.AuthorRepository;
import com.tadpole.poem.service.util.GrabPageProcessor;
import org.apache.commons.lang3.StringUtils;
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
            String fullFilePath = configurationRepository.findByIdentifier("AUTHOR-AVATAR-BATH").getContent() + realFileName;

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

        for (Author author: authorRepository.findAll()) {

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
//        for (Poem poem: poems) {
//            poem.getAuthor();
//        }

        String stringFilesPath = configurationRepository.findByIdentifier("JSON_FILE_PATH").getContent();
        try {
            objectMapper.writeValue(new File(stringFilesPath + "Poems.json"), poems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
