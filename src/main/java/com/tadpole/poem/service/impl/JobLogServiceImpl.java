package com.tadpole.poem.service.impl;

import com.tadpole.poem.service.JobLogService;
import com.tadpole.poem.domain.JobLog;
import com.tadpole.poem.repository.JobLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing JobLog.
 */
@Service
public class JobLogServiceImpl implements JobLogService{

    private final Logger log = LoggerFactory.getLogger(JobLogServiceImpl.class);

    @Inject
    private JobLogRepository jobLogRepository;

    /**
     * Save a jobLog.
     *
     * @param jobLog the entity to save
     * @return the persisted entity
     */
    public JobLog save(JobLog jobLog) {
        log.debug("Request to save JobLog : {}", jobLog);
        JobLog result = jobLogRepository.save(jobLog);
        return result;
    }

    /**
     *  Get all the jobLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<JobLog> findAll(Pageable pageable) {
        log.debug("Request to get all JobLogs");
        Page<JobLog> result = jobLogRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one jobLog by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public JobLog findOne(Long id) {
        log.debug("Request to get JobLog : {}", id);
        JobLog jobLog = jobLogRepository.findOne(id);
        return jobLog;
    }

    /**
     *  Delete the  jobLog by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JobLog : {}", id);
        jobLogRepository.delete(id);
    }
}
