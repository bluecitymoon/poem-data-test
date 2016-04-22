package com.tadpole.poem.service;

import com.tadpole.poem.domain.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing JobLog.
 */
public interface JobLogService {

    /**
     * Save a jobLog.
     * 
     * @param jobLog the entity to save
     * @return the persisted entity
     */
    JobLog save(JobLog jobLog);

    /**
     *  Get all the jobLogs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JobLog> findAll(Pageable pageable);

    /**
     *  Get the "id" jobLog.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    JobLog findOne(Long id);

    /**
     *  Delete the "id" jobLog.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
