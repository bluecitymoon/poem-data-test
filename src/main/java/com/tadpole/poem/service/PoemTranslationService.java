package com.tadpole.poem.service;

import com.tadpole.poem.domain.PoemTranslation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing PoemTranslation.
 */
public interface PoemTranslationService {

    /**
     * Save a poemTranslation.
     * 
     * @param poemTranslation the entity to save
     * @return the persisted entity
     */
    PoemTranslation save(PoemTranslation poemTranslation);

    /**
     *  Get all the poemTranslations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PoemTranslation> findAll(Pageable pageable);

    /**
     *  Get the "id" poemTranslation.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    PoemTranslation findOne(Long id);

    /**
     *  Delete the "id" poemTranslation.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
