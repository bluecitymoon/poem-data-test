package com.tadpole.poem.web.rest;

import com.tadpole.poem.PoemdataApp;
import com.tadpole.poem.domain.DetailResource;
import com.tadpole.poem.repository.DetailResourceRepository;
import com.tadpole.poem.service.DetailResourceService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DetailResourceResource REST controller.
 *
 * @see DetailResourceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PoemdataApp.class)
@WebAppConfiguration
@IntegrationTest
public class DetailResourceResourceIntTest {

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";
    private static final String DEFAULT_OUTSIDE_ID = "AAAAA";
    private static final String UPDATED_OUTSIDE_ID = "BBBBB";

    private static final Integer DEFAULT_VISIT_COUNT = 1;
    private static final Integer UPDATED_VISIT_COUNT = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    @Inject
    private DetailResourceRepository detailResourceRepository;

    @Inject
    private DetailResourceService detailResourceService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDetailResourceMockMvc;

    private DetailResource detailResource;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DetailResourceResource detailResourceResource = new DetailResourceResource();
        ReflectionTestUtils.setField(detailResourceResource, "detailResourceService", detailResourceService);
        this.restDetailResourceMockMvc = MockMvcBuilders.standaloneSetup(detailResourceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        detailResource = new DetailResource();
        detailResource.setUrl(DEFAULT_URL);
        detailResource.setOutsideId(DEFAULT_OUTSIDE_ID);
        detailResource.setVisitCount(DEFAULT_VISIT_COUNT);
        detailResource.setActive(DEFAULT_ACTIVE);
        detailResource.setTitle(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createDetailResource() throws Exception {
        int databaseSizeBeforeCreate = detailResourceRepository.findAll().size();

        // Create the DetailResource

        restDetailResourceMockMvc.perform(post("/api/detail-resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(detailResource)))
                .andExpect(status().isCreated());

        // Validate the DetailResource in the database
        List<DetailResource> detailResources = detailResourceRepository.findAll();
        assertThat(detailResources).hasSize(databaseSizeBeforeCreate + 1);
        DetailResource testDetailResource = detailResources.get(detailResources.size() - 1);
        assertThat(testDetailResource.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDetailResource.getOutsideId()).isEqualTo(DEFAULT_OUTSIDE_ID);
        assertThat(testDetailResource.getVisitCount()).isEqualTo(DEFAULT_VISIT_COUNT);
        assertThat(testDetailResource.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testDetailResource.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void getAllDetailResources() throws Exception {
        // Initialize the database
        detailResourceRepository.saveAndFlush(detailResource);

        // Get all the detailResources
        restDetailResourceMockMvc.perform(get("/api/detail-resources?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(detailResource.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].outsideId").value(hasItem(DEFAULT_OUTSIDE_ID.toString())))
                .andExpect(jsonPath("$.[*].visitCount").value(hasItem(DEFAULT_VISIT_COUNT)))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getDetailResource() throws Exception {
        // Initialize the database
        detailResourceRepository.saveAndFlush(detailResource);

        // Get the detailResource
        restDetailResourceMockMvc.perform(get("/api/detail-resources/{id}", detailResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(detailResource.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.outsideId").value(DEFAULT_OUTSIDE_ID.toString()))
            .andExpect(jsonPath("$.visitCount").value(DEFAULT_VISIT_COUNT))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDetailResource() throws Exception {
        // Get the detailResource
        restDetailResourceMockMvc.perform(get("/api/detail-resources/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDetailResource() throws Exception {
        // Initialize the database
        detailResourceService.save(detailResource);

        int databaseSizeBeforeUpdate = detailResourceRepository.findAll().size();

        // Update the detailResource
        DetailResource updatedDetailResource = new DetailResource();
        updatedDetailResource.setId(detailResource.getId());
        updatedDetailResource.setUrl(UPDATED_URL);
        updatedDetailResource.setOutsideId(UPDATED_OUTSIDE_ID);
        updatedDetailResource.setVisitCount(UPDATED_VISIT_COUNT);
        updatedDetailResource.setActive(UPDATED_ACTIVE);
        updatedDetailResource.setTitle(UPDATED_TITLE);

        restDetailResourceMockMvc.perform(put("/api/detail-resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDetailResource)))
                .andExpect(status().isOk());

        // Validate the DetailResource in the database
        List<DetailResource> detailResources = detailResourceRepository.findAll();
        assertThat(detailResources).hasSize(databaseSizeBeforeUpdate);
        DetailResource testDetailResource = detailResources.get(detailResources.size() - 1);
        assertThat(testDetailResource.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDetailResource.getOutsideId()).isEqualTo(UPDATED_OUTSIDE_ID);
        assertThat(testDetailResource.getVisitCount()).isEqualTo(UPDATED_VISIT_COUNT);
        assertThat(testDetailResource.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testDetailResource.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void deleteDetailResource() throws Exception {
        // Initialize the database
        detailResourceService.save(detailResource);

        int databaseSizeBeforeDelete = detailResourceRepository.findAll().size();

        // Get the detailResource
        restDetailResourceMockMvc.perform(delete("/api/detail-resources/{id}", detailResource.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DetailResource> detailResources = detailResourceRepository.findAll();
        assertThat(detailResources).hasSize(databaseSizeBeforeDelete - 1);
    }
}
