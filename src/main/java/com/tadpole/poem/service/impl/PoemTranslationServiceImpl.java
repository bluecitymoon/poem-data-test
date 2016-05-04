package com.tadpole.poem.service.impl;

import com.tadpole.poem.service.PoemTranslationService;
import com.tadpole.poem.domain.PoemTranslation;
import com.tadpole.poem.repository.PoemTranslationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing PoemTranslation.
 */
@Service
@Transactional
public class PoemTranslationServiceImpl implements PoemTranslationService{

    private final Logger log = LoggerFactory.getLogger(PoemTranslationServiceImpl.class);
    
    @Inject
    private PoemTranslationRepository poemTranslationRepository;
    
    /**
     * Save a poemTranslation.
     * 
     * @param poemTranslation the entity to save
     * @return the persisted entity
     */
    public PoemTranslation save(PoemTranslation poemTranslation) {
        log.debug("Request to save PoemTranslation : {}", poemTranslation);
        PoemTranslation result = poemTranslationRepository.save(poemTranslation);
        return result;
    }

    /**
     *  Get all the poemTranslations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PoemTranslation> findAll(Pageable pageable) {
        log.debug("Request to get all PoemTranslations");
        Page<PoemTranslation> result = poemTranslationRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one poemTranslation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PoemTranslation findOne(Long id) {
        log.debug("Request to get PoemTranslation : {}", id);
        PoemTranslation poemTranslation = poemTranslationRepository.findOne(id);
        return poemTranslation;
    }

    /**
     *  Delete the  poemTranslation by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PoemTranslation : {}", id);
        poemTranslationRepository.delete(id);
    }
}
