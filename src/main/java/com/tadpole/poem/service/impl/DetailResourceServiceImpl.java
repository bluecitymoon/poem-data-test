package com.tadpole.poem.service.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.tadpole.poem.domain.Job;
import com.tadpole.poem.domain.JobLog;
import com.tadpole.poem.repository.JobLogRepository;
import com.tadpole.poem.service.DetailResourceService;
import com.tadpole.poem.domain.DetailResource;
import com.tadpole.poem.repository.DetailResourceRepository;
import com.tadpole.poem.service.util.GrabPageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Implementation for managing DetailResource.
 */
@Service
@Transactional
public class DetailResourceServiceImpl implements DetailResourceService {

    private final Logger log = LoggerFactory.getLogger(DetailResourceServiceImpl.class);

    @Inject
    private DetailResourceRepository detailResourceRepository;

    @Inject
    private JobLogRepository jobLogRepository;

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

            List<DetailResource> page = GrabPageProcessor.getPoemDetailUrls(job, webClient, i);

            detailResourceRepository.save(page);

            jobLog.setMessage(page.toString() + "saved.");

            jobLog.setEnd(ZonedDateTime.now());


            jobLogRepository.save(jobLog);
        }
        return false;
    }
}
