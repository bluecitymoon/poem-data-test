package com.tadpole.poem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tadpole.poem.domain.PoemTranslation;
import com.tadpole.poem.service.PoemTranslationService;
import com.tadpole.poem.web.rest.util.HeaderUtil;
import com.tadpole.poem.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PoemTranslation.
 */
@RestController
@RequestMapping("/api")
public class PoemTranslationResource {

    private final Logger log = LoggerFactory.getLogger(PoemTranslationResource.class);
        
    @Inject
    private PoemTranslationService poemTranslationService;
    
    /**
     * POST  /poem-translations : Create a new poemTranslation.
     *
     * @param poemTranslation the poemTranslation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new poemTranslation, or with status 400 (Bad Request) if the poemTranslation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/poem-translations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PoemTranslation> createPoemTranslation(@Valid @RequestBody PoemTranslation poemTranslation) throws URISyntaxException {
        log.debug("REST request to save PoemTranslation : {}", poemTranslation);
        if (poemTranslation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("poemTranslation", "idexists", "A new poemTranslation cannot already have an ID")).body(null);
        }
        PoemTranslation result = poemTranslationService.save(poemTranslation);
        return ResponseEntity.created(new URI("/api/poem-translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("poemTranslation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /poem-translations : Updates an existing poemTranslation.
     *
     * @param poemTranslation the poemTranslation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated poemTranslation,
     * or with status 400 (Bad Request) if the poemTranslation is not valid,
     * or with status 500 (Internal Server Error) if the poemTranslation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/poem-translations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PoemTranslation> updatePoemTranslation(@Valid @RequestBody PoemTranslation poemTranslation) throws URISyntaxException {
        log.debug("REST request to update PoemTranslation : {}", poemTranslation);
        if (poemTranslation.getId() == null) {
            return createPoemTranslation(poemTranslation);
        }
        PoemTranslation result = poemTranslationService.save(poemTranslation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("poemTranslation", poemTranslation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /poem-translations : get all the poemTranslations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of poemTranslations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/poem-translations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PoemTranslation>> getAllPoemTranslations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PoemTranslations");
        Page<PoemTranslation> page = poemTranslationService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/poem-translations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /poem-translations/:id : get the "id" poemTranslation.
     *
     * @param id the id of the poemTranslation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the poemTranslation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/poem-translations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PoemTranslation> getPoemTranslation(@PathVariable Long id) {
        log.debug("REST request to get PoemTranslation : {}", id);
        PoemTranslation poemTranslation = poemTranslationService.findOne(id);
        return Optional.ofNullable(poemTranslation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /poem-translations/:id : delete the "id" poemTranslation.
     *
     * @param id the id of the poemTranslation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/poem-translations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePoemTranslation(@PathVariable Long id) {
        log.debug("REST request to delete PoemTranslation : {}", id);
        poemTranslationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("poemTranslation", id.toString())).build();
    }

}
