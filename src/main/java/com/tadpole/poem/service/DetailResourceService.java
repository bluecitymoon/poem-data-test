package com.tadpole.poem.service;

import com.tadpole.poem.domain.DetailResource;
import com.tadpole.poem.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing DetailResource.
 */
public interface DetailResourceService {

    /**
     * Save a detailResource.
     *
     * @param detailResource the entity to save
     * @return the persisted entity
     */
    DetailResource save(DetailResource detailResource);

    /**
     *  Get all the detailResources.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DetailResource> findAll(Pageable pageable);

    /**
     *  Get the "id" detailResource.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DetailResource findOne(Long id);

    /**
     *  Delete the "id" detailResource.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    boolean grabAllDetailLinks(Job job);

    void grabDetailLinksInTypes(Job job);
}
