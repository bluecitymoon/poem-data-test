package com.tadpole.poem.service;

import com.tadpole.poem.domain.Poem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Poem.
 */
public interface PoemService {

    /**
     * Save a poem.
     * 
     * @param poem the entity to save
     * @return the persisted entity
     */
    Poem save(Poem poem);

    /**
     *  Get all the poems.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Poem> findAll(Pageable pageable);

    /**
     *  Get the "id" poem.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Poem findOne(Long id);

    /**
     *  Delete the "id" poem.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
