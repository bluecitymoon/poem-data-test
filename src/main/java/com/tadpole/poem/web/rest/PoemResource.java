package com.tadpole.poem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tadpole.poem.domain.Poem;
import com.tadpole.poem.service.PoemService;
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
 * REST controller for managing Poem.
 */
@RestController
@RequestMapping("/api")
public class PoemResource {

    private final Logger log = LoggerFactory.getLogger(PoemResource.class);

    @Inject
    private PoemService poemService;

    /**
     * POST  /poems : Create a new poem.
     *
     * @param poem the poem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new poem, or with status 400 (Bad Request) if the poem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/poems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Poem> createPoem(@Valid @RequestBody Poem poem) throws URISyntaxException {
        log.debug("REST request to save Poem : {}", poem);
        if (poem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("poem", "idexists", "A new poem cannot already have an ID")).body(null);
        }
        Poem result = poemService.save(poem);
        return ResponseEntity.created(new URI("/api/poems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("poem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /poems : Updates an existing poem.
     *
     * @param poem the poem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated poem,
     * or with status 400 (Bad Request) if the poem is not valid,
     * or with status 500 (Internal Server Error) if the poem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/poems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Poem> updatePoem(@Valid @RequestBody Poem poem) throws URISyntaxException {
        log.debug("REST request to update Poem : {}", poem);
        if (poem.getId() == null) {
            return createPoem(poem);
        }
        Poem result = poemService.save(poem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("poem", poem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /poems : get all the poems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of poems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/poems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Poem>> getAllPoems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Poems");
        Page<Poem> page = poemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/poems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /poems/:id : get the "id" poem.
     *
     * @param id the id of the poem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the poem, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/poems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Poem> getPoem(@PathVariable Long id) {
        log.debug("REST request to get Poem : {}", id);
        Poem poem = poemService.findOne(id);
        return Optional.ofNullable(poem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /poems/:id : delete the "id" poem.
     *
     * @param id the id of the poem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/poems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePoem(@PathVariable Long id) {
        log.debug("REST request to delete Poem : {}", id);
        poemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("poem", id.toString())).build();
    }

}
