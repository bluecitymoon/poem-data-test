package com.tadpole.poem.service;

import com.tadpole.poem.domain.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Story.
 */
public interface StoryService {

    /**
     * Save a story.
     * 
     * @param story the entity to save
     * @return the persisted entity
     */
    Story save(Story story);

    /**
     *  Get all the stories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Story> findAll(Pageable pageable);

    /**
     *  Get the "id" story.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Story findOne(Long id);

    /**
     *  Delete the "id" story.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
