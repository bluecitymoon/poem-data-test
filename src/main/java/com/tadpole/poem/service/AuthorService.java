package com.tadpole.poem.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.tadpole.poem.domain.Author;
import com.tadpole.poem.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Author.
 */
public interface AuthorService {

    /**
     * Save a author.
     *
     * @param author the entity to save
     * @return the persisted entity
     */
    Author save(Author author);

    /**
     *  Get all the authors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Author> findAll(Pageable pageable);

    /**
     *  Get the "id" author.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Author findOne(Long id);

    /**
     *  Delete the "id" author.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    boolean fillUpAuthorInformation(Job job);

    Author fillUpSingleAuthor(Author author, WebClient webClient);

    void downloadAvatars(Job job, WebClient webclient);

    void objectsToJsonFiles(Job job);
}
