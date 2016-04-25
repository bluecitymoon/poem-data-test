package com.tadpole.poem.service.impl;

import com.tadpole.poem.service.PoemService;
import com.tadpole.poem.domain.Poem;
import com.tadpole.poem.repository.PoemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Poem.
 */
@Service
@Transactional
public class PoemServiceImpl implements PoemService{

    private final Logger log = LoggerFactory.getLogger(PoemServiceImpl.class);
    
    @Inject
    private PoemRepository poemRepository;
    
    /**
     * Save a poem.
     * 
     * @param poem the entity to save
     * @return the persisted entity
     */
    public Poem save(Poem poem) {
        log.debug("Request to save Poem : {}", poem);
        Poem result = poemRepository.save(poem);
        return result;
    }

    /**
     *  Get all the poems.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Poem> findAll(Pageable pageable) {
        log.debug("Request to get all Poems");
        Page<Poem> result = poemRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one poem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Poem findOne(Long id) {
        log.debug("Request to get Poem : {}", id);
        Poem poem = poemRepository.findOne(id);
        return poem;
    }

    /**
     *  Delete the  poem by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Poem : {}", id);
        poemRepository.delete(id);
    }
}
