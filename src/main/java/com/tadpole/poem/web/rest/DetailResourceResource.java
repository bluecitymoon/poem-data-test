package com.tadpole.poem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tadpole.poem.domain.DetailResource;
import com.tadpole.poem.service.DetailResourceService;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DetailResource.
 */
@RestController
@RequestMapping("/api")
public class DetailResourceResource {

    private final Logger log = LoggerFactory.getLogger(DetailResourceResource.class);
        
    @Inject
    private DetailResourceService detailResourceService;
    
    /**
     * POST  /detail-resources : Create a new detailResource.
     *
     * @param detailResource the detailResource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new detailResource, or with status 400 (Bad Request) if the detailResource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/detail-resources",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DetailResource> createDetailResource(@RequestBody DetailResource detailResource) throws URISyntaxException {
        log.debug("REST request to save DetailResource : {}", detailResource);
        if (detailResource.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("detailResource", "idexists", "A new detailResource cannot already have an ID")).body(null);
        }
        DetailResource result = detailResourceService.save(detailResource);
        return ResponseEntity.created(new URI("/api/detail-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("detailResource", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /detail-resources : Updates an existing detailResource.
     *
     * @param detailResource the detailResource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated detailResource,
     * or with status 400 (Bad Request) if the detailResource is not valid,
     * or with status 500 (Internal Server Error) if the detailResource couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/detail-resources",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DetailResource> updateDetailResource(@RequestBody DetailResource detailResource) throws URISyntaxException {
        log.debug("REST request to update DetailResource : {}", detailResource);
        if (detailResource.getId() == null) {
            return createDetailResource(detailResource);
        }
        DetailResource result = detailResourceService.save(detailResource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("detailResource", detailResource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /detail-resources : get all the detailResources.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of detailResources in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/detail-resources",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DetailResource>> getAllDetailResources(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DetailResources");
        Page<DetailResource> page = detailResourceService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/detail-resources");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /detail-resources/:id : get the "id" detailResource.
     *
     * @param id the id of the detailResource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the detailResource, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/detail-resources/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DetailResource> getDetailResource(@PathVariable Long id) {
        log.debug("REST request to get DetailResource : {}", id);
        DetailResource detailResource = detailResourceService.findOne(id);
        return Optional.ofNullable(detailResource)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /detail-resources/:id : delete the "id" detailResource.
     *
     * @param id the id of the detailResource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/detail-resources/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDetailResource(@PathVariable Long id) {
        log.debug("REST request to delete DetailResource : {}", id);
        detailResourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("detailResource", id.toString())).build();
    }

}
