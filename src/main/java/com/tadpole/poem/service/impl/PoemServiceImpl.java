package com.tadpole.poem.service.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.tadpole.poem.domain.Author;
import com.tadpole.poem.domain.DetailResource;
import com.tadpole.poem.domain.Job;
import com.tadpole.poem.repository.AuthorRepository;
import com.tadpole.poem.repository.DetailResourceRepository;
import com.tadpole.poem.service.AuthorService;
import com.tadpole.poem.service.PoemService;
import com.tadpole.poem.domain.Poem;
import com.tadpole.poem.repository.PoemRepository;
import com.tadpole.poem.service.util.GrabPageProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Service Implementation for managing Poem.
 */
@Service
public class PoemServiceImpl implements PoemService {

    private final Logger log = LoggerFactory.getLogger(PoemServiceImpl.class);

    @Inject
    private PoemRepository poemRepository;

    @Inject
    private DetailResourceRepository detailResourceRepository;

    @Inject
    private AuthorRepository authorRepository;

    @Inject
    private AuthorService authorService;

    /**
     * Save a poem.
     *
     * @param poem the entity to save
     * @return the persisted entity
     */
    @Transactional
    public Poem save(Poem poem) {
        log.debug("Request to save Poem : {}", poem);
        Poem result = poemRepository.save(poem);
        return result;
    }

    /**
     * Get all the poems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Poem> findAll(Pageable pageable) {
        log.debug("Request to get all Poems");
        Page<Poem> result = poemRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one poem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Poem findOne(Long id) {
        log.debug("Request to get Poem : {}", id);
        Poem poem = poemRepository.findOne(id);
        return poem;
    }

    /**
     * Delete the  poem by id.
     *
     * @param id the id of the entity
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Poem : {}", id);
        poemRepository.delete(id);
    }

    /**
     * @param job
     * @param detailResource
     * @return
     */
    public Poem grabSinglePoem(Job job, DetailResource detailResource, WebClient webClient) {


        String fullUrl = job.getTarget() + detailResource.getUrl();

        Poem poem = new Poem();
        try {
            HtmlPage htmlPage = webClient.getPage(new URL(fullUrl));


            Document document = Jsoup.parse(htmlPage.getWebResponse().getContentAsString());
            Element element = document.getElementsByClass("son2").last();

            Elements elements = element.getElementsByTag("p");
            Element[] objects = (Element[]) elements.toArray();

            String periodElement = objects[0].text().substring("朝代：".length());
            Elements authorElements = objects[1].children();
            Element authorElement = null;
            String authorName = "";
            if (authorElements.size() == 2) {
                authorElement = authorElements.last();
                authorName = authorElement.text();

            } else {
                authorName = objects[1].text().substring("作者".length());
            }
            Element contentElement = null;

            if (objects.length > 3) {
                contentElement = elements.last();
            }



            String poemContentXpath = "//*[contains(concat(\" \", normalize-space(@class), \" \"), \" son2 \")]";
            List<HtmlDivision> divisions = (List<HtmlDivision>) htmlPage.getByXPath(poemContentXpath);

            for (HtmlDivision division : divisions) {

                String text = division.getTextContent();
                if (text.contains("原文：")) {
                    int start = text.indexOf("原文：") + "原文：".length();

                    String content = text.substring(start);

                    poem.setContent(content.trim());
                    poem.setTitle(detailResource.getTitle());

                    List<HtmlAnchor> anchors = (List<HtmlAnchor>) division.getByXPath(poemContentXpath + "//a");

                    for (HtmlAnchor htmlAnchor : anchors) {

                        String href = htmlAnchor.getHrefAttribute();

                        if (href.startsWith("/author_")) {

                            Author author = authorRepository.findByLink(href);

                            if (author == null) {

                                Author newAuthor = new Author();
                                newAuthor.setLink(href);
                                newAuthor.setName(htmlAnchor.getTextContent().trim());

                                Author savedAuthor = authorService.save(newAuthor);

                                poem.setAuthor(savedAuthor);
                            } else {
                                poem.setAuthor(author);
                            }
                            poem.setAnthorName(htmlAnchor.getTextContent());
                        }
                    }
                }

            }

        } catch (IOException e) {

            System.out.println(e.getMessage());

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

        poemRepository.save(poem);

        return poem;
    }

    /**
     * @param job
     * @return
     */
    public boolean grabAllPoems(Job job) {
        List<DetailResource> urls = detailResourceRepository.findByVisitCountIsNull();

        WebClient webClient = GrabPageProcessor.newWebClient();

        for (DetailResource detailResource : urls) {
            grabSinglePoem(job, detailResource, webClient);

            Integer visitCount = detailResource.getVisitCount();
            if (visitCount == null) {
                detailResource.setVisitCount(1);
            } else {
                detailResource.setVisitCount(visitCount + 1);
            }

            detailResourceRepository.save(detailResource);
        }
        return false;
    }
}
