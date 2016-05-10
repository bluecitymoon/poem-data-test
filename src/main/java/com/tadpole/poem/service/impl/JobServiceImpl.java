package com.tadpole.poem.service.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.tadpole.poem.service.*;
import com.tadpole.poem.domain.Job;
import com.tadpole.poem.repository.JobRepository;
import com.tadpole.poem.service.util.GrabPageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Implementation for managing Job.
 */
@Service
//@Transactional
public class JobServiceImpl implements JobService {

    private final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    @Inject
    private JobRepository jobRepository;

    @Inject
    private DetailResourceService detailResourceService;

    @Inject
    private PoemService poemService;

    @Inject
    private AuthorService authorService;

    @Inject
    private TagService tagService;

    /**
     * Save a job.
     *
     * @param job the entity to save
     * @return the persisted entity
     */
    public Job save(Job job) {
        log.debug("Request to save Job : {}", job);
        Job result = jobRepository.save(job);
        return result;
    }

    /**
     * Get all the jobs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Job> findAll(Pageable pageable) {
        log.debug("Request to get all Jobs");
        Page<Job> result = jobRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one job by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Job findOne(Long id) {
        log.debug("Request to get Job : {}", id);
        Job job = jobRepository.findOne(id);
        return job;
    }

    /**
     * Delete the  job by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Job : {}", id);
        jobRepository.delete(id);
    }

    @Override
    public boolean start(Job job) {

        job.setLastStart(ZonedDateTime.now());

        switch (job.getIdentifier()) {
            case "POEM-DETAIL":

                detailResourceService.grabAllDetailLinks(job);

                job.setLastStop(ZonedDateTime.now());

                jobRepository.save(job);

                break;

            case "POEM-CONTENT":

                poemService.grabAllPoems(job);

                job.setLastStop(ZonedDateTime.now());

                jobRepository.save(job);

                break;

            case "AUTHOR-FILL-UP":

                authorService.fillUpAuthorInformation(job);

                job.setLastStop(ZonedDateTime.now());

                jobRepository.save(job);
                break;
            case "DOWNLOAD-AUTHOR-AVATAR":

                authorService.downloadAvatars(job, GrabPageProcessor.newWebClient());

                job.setLastStop(ZonedDateTime.now());

                jobRepository.save(job);
                break;

            case "OBJECTS-TO-JSON-file":

                authorService.objectsToJsonFiles(job);

                job.setLastStop(ZonedDateTime.now());

                jobRepository.save(job);

                break;

            case "TAGS":
                tagService.grabAllTags(job, GrabPageProcessor.newWebClient());

                job.setLastStop(ZonedDateTime.now());

                jobRepository.save(job);

                break;
            case "Poem-detail-in-tags":

                detailResourceService.grabDetailLinksInTypes(job);
                job.setLastStop(ZonedDateTime.now());

                jobRepository.save(job);
                break;

            case "get-poem-resource-by-author":

                detailResourceService.grabAllDetailByAuthor(job);

                job.setLastStop(ZonedDateTime.now());

                jobRepository.save(job);
                break;

            default:
                break;
        }

        return false;
    }

}
