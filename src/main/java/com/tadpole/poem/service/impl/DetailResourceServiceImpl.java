package com.tadpole.poem.service.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.google.common.collect.Lists;
import com.tadpole.poem.domain.*;
import com.tadpole.poem.domain.enumeration.JobExecutionResult;
import com.tadpole.poem.repository.AuthorRepository;
import com.tadpole.poem.repository.JobLogRepository;
import com.tadpole.poem.repository.TagRepository;
import com.tadpole.poem.service.DetailResourceService;
import com.tadpole.poem.repository.DetailResourceRepository;
import com.tadpole.poem.service.util.GrabPageProcessor;
import com.tadpole.poem.service.util.MathUtil;
import com.tadpole.poem.service.util.PinyinTranslator;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * Service Implementation for managing DetailResource.
 */
@Service
public class DetailResourceServiceImpl implements DetailResourceService {

    private final Logger log = LoggerFactory.getLogger(DetailResourceServiceImpl.class);

    @Inject
    private DetailResourceRepository detailResourceRepository;

    @Inject
    private JobLogRepository jobLogRepository;

    @Inject
    private TagRepository tagRepository;

    @Inject
    private AuthorRepository authorRepository;

    /**
     * Save a detailResource.
     *
     * @param detailResource the entity to save
     * @return the persisted entity
     */
    public DetailResource save(DetailResource detailResource) {
        log.debug("Request to save DetailResource : {}", detailResource);
        DetailResource result = detailResourceRepository.save(detailResource);
        return result;
    }

    /**
     * Get all the detailResources.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DetailResource> findAll(Pageable pageable) {
        log.debug("Request to get all DetailResources");
        Page<DetailResource> result = detailResourceRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one detailResource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public DetailResource findOne(Long id) {
        log.debug("Request to get DetailResource : {}", id);
        DetailResource detailResource = detailResourceRepository.findOne(id);
        return detailResource;
    }

    /**
     * Delete the  detailResource by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DetailResource : {}", id);
        detailResourceRepository.delete(id);
    }

    @Override
    public boolean grabAllDetailLinks(Job job) {

        WebClient webClient = GrabPageProcessor.newWebClient();

        for (int i = 1; i < 201; i++) {

            JobLog jobLog = new JobLog();
            jobLog.setJob(job);
            jobLog.setStart(ZonedDateTime.now());

            List<DetailResource> page = GrabPageProcessor.getPoemDetailUrls(job, webClient, i, null);

            detailResourceRepository.save(page);

            jobLog.setMessage(page.toString() + "saved.");

            jobLog.setEnd(ZonedDateTime.now());


            jobLogRepository.save(jobLog);
        }
        return false;
    }

    @Override
    public void grabAllDetailByAuthor(Job job) {

        List<Author> authors = authorRepository.findAll();
        authors.forEach(author -> {

            int i = 1;

            while (true) {

                String fullUrl = job.getTarget() + author.getName() + "&page=" + i;

                try {
                    Document document = Jsoup.connect(fullUrl).get();

                    List<org.jsoup.nodes.Element> elements = Lists.newArrayList();
                    Elements elementsInPage = document.getElementsByTag("a");
                    for (org.jsoup.nodes.Element url: elementsInPage) {

                        String href = url.attr("href");
                        if (StringUtils.isNotEmpty(href) && href.startsWith("/view")) {
                            elements.add(url);
                        }
                    }
                    if (elements.isEmpty()) {
                        break;
                    }

                    for (org.jsoup.nodes.Element url : elements) {

                        String href = url.attr("href");
                        DetailResource detailResource = detailResourceRepository.findByUrl(href);

                        if (detailResource == null) {

                            detailResource = new DetailResource();
                            detailResource.setTitle(url.text());
                            detailResource.setOutsideId(MathUtil.getNumber(href).toString());
                            detailResource.setUrl(href);

                            detailResourceRepository.save(detailResource);

                            JobLog jobLog = new JobLog();
                            jobLog.setMessage("saved new resource " + detailResource.getTitle());
                            jobLog.setJob(job);

                            jobLogRepository.save(jobLog);
                        } else {

                            JobLog jobLog = new JobLog();
                            jobLog.setMessage("skip saving existed" + detailResource.getTitle());
                            jobLog.setJob(job);

                            jobLogRepository.save(jobLog);
                        }
                    }

                    i++;

                } catch (IOException e) {

                    JobLog jobLog = new JobLog();
                    jobLog.setMessage("get document failed " + fullUrl + " Exception " + e.getMessage());
                    jobLog.setResult(JobExecutionResult.fail);

                    jobLog.setJob(job);

                    jobLogRepository.save(jobLog);
                }
            }

        });
    }

    @Override
    public void grabDetailLinksInTypes(Job job) {
        WebClient webClient = GrabPageProcessor.newWebClient();
        List<Tag> tags = tagRepository.findAll();

        for (Tag tag : tags) {
            System.err.println("Visiting tag " + tag.getIdentifier());
            int i = 1;
            for (; ; ) {
                List<DetailResource> page = GrabPageProcessor.getPoemDetailUrls(job, webClient, i, tag.getIdentifier());

                if (page == null) break;

                page.forEach(p -> p.setTag(tag.getIdentifier()));

                saveDetailResource(page, job);

                i++;
            }
        }
    }

    private void saveDetailResource(List<DetailResource> page, Job job) {

        for (DetailResource detailResource : page) {

            DetailResource resource = detailResourceRepository.findByUrl(detailResource.getUrl());
            if (resource == null) {
                DetailResource detail = detailResourceRepository.save(detailResource);

                JobLog log = new JobLog();
                log.setMessage(detail.toString() + " saved with job " + job.getName());
                log.setEnd(ZonedDateTime.now());

                jobLogRepository.save(log);
            } else {

                JobLog log = new JobLog();
                log.setMessage("Skip to save resource " + resource.getTitle() + " reason: existed.");
                log.setEnd(ZonedDateTime.now());

                jobLogRepository.save(log);
            }
        }
    }
}
