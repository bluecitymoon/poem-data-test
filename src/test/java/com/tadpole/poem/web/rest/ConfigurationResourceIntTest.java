package com.tadpole.poem.web.rest;

import com.tadpole.poem.PoemdataApp;
import com.tadpole.poem.domain.Configuration;
import com.tadpole.poem.repository.ConfigurationRepository;

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
 * Test class for the ConfigurationResource REST controller.
 *
 * @see ConfigurationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PoemdataApp.class)
@WebAppConfiguration
@IntegrationTest
public class ConfigurationResourceIntTest {

    private static final String DEFAULT_IDENTIFIER = "AAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBB";
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    @Inject
    private ConfigurationRepository configurationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restConfigurationMockMvc;

    private Configuration configuration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConfigurationResource configurationResource = new ConfigurationResource();
        ReflectionTestUtils.setField(configurationResource, "configurationRepository", configurationRepository);
        this.restConfigurationMockMvc = MockMvcBuilders.standaloneSetup(configurationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        configuration = new Configuration();
        configuration.setIdentifier(DEFAULT_IDENTIFIER);
        configuration.setContent(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createConfiguration() throws Exception {
        int databaseSizeBeforeCreate = configurationRepository.findAll().size();

        // Create the Configuration

        restConfigurationMockMvc.perform(post("/api/configurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(configuration)))
                .andExpect(status().isCreated());

        // Validate the Configuration in the database
        List<Configuration> configurations = configurationRepository.findAll();
        assertThat(configurations).hasSize(databaseSizeBeforeCreate + 1);
        Configuration testConfiguration = configurations.get(configurations.size() - 1);
        assertThat(testConfiguration.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testConfiguration.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void getAllConfigurations() throws Exception {
        // Initialize the database
        configurationRepository.saveAndFlush(configuration);

        // Get all the configurations
        restConfigurationMockMvc.perform(get("/api/configurations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(configuration.getId().intValue())))
                .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getConfiguration() throws Exception {
        // Initialize the database
        configurationRepository.saveAndFlush(configuration);

        // Get the configuration
        restConfigurationMockMvc.perform(get("/api/configurations/{id}", configuration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(configuration.getId().intValue()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfiguration() throws Exception {
        // Get the configuration
        restConfigurationMockMvc.perform(get("/api/configurations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfiguration() throws Exception {
        // Initialize the database
        configurationRepository.saveAndFlush(configuration);
        int databaseSizeBeforeUpdate = configurationRepository.findAll().size();

        // Update the configuration
        Configuration updatedConfiguration = new Configuration();
        updatedConfiguration.setId(configuration.getId());
        updatedConfiguration.setIdentifier(UPDATED_IDENTIFIER);
        updatedConfiguration.setContent(UPDATED_CONTENT);

        restConfigurationMockMvc.perform(put("/api/configurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedConfiguration)))
                .andExpect(status().isOk());

        // Validate the Configuration in the database
        List<Configuration> configurations = configurationRepository.findAll();
        assertThat(configurations).hasSize(databaseSizeBeforeUpdate);
        Configuration testConfiguration = configurations.get(configurations.size() - 1);
        assertThat(testConfiguration.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testConfiguration.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void deleteConfiguration() throws Exception {
        // Initialize the database
        configurationRepository.saveAndFlush(configuration);
        int databaseSizeBeforeDelete = configurationRepository.findAll().size();

        // Get the configuration
        restConfigurationMockMvc.perform(delete("/api/configurations/{id}", configuration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Configuration> configurations = configurationRepository.findAll();
        assertThat(configurations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
