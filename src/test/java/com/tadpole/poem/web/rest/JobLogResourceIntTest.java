package com.tadpole.poem.web.rest;

import com.tadpole.poem.PoemdataApp;
import com.tadpole.poem.domain.JobLog;
import com.tadpole.poem.repository.JobLogRepository;
import com.tadpole.poem.service.JobLogService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tadpole.poem.domain.enumeration.JobExecutionResult;

/**
 * Test class for the JobLogResource REST controller.
 *
 * @see JobLogResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PoemdataApp.class)
@WebAppConfiguration
@IntegrationTest
public class JobLogResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final JobExecutionResult DEFAULT_RESULT = JobExecutionResult.fail;
    private static final JobExecutionResult UPDATED_RESULT = JobExecutionResult.success;

    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_STR = dateTimeFormatter.format(DEFAULT_START);

    private static final ZonedDateTime DEFAULT_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_STR = dateTimeFormatter.format(DEFAULT_END);
    private static final String DEFAULT_MESSAGE = "AAAAA";
    private static final String UPDATED_MESSAGE = "BBBBB";

    @Inject
    private JobLogRepository jobLogRepository;

    @Inject
    private JobLogService jobLogService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJobLogMockMvc;

    private JobLog jobLog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobLogResource jobLogResource = new JobLogResource();
        ReflectionTestUtils.setField(jobLogResource, "jobLogService", jobLogService);
        this.restJobLogMockMvc = MockMvcBuilders.standaloneSetup(jobLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        jobLog = new JobLog();
        jobLog.setResult(DEFAULT_RESULT);
        jobLog.setStart(DEFAULT_START);
        jobLog.setEnd(DEFAULT_END);
        jobLog.setMessage(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void createJobLog() throws Exception {
        int databaseSizeBeforeCreate = jobLogRepository.findAll().size();

        // Create the JobLog

        restJobLogMockMvc.perform(post("/api/job-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobLog)))
                .andExpect(status().isCreated());

        // Validate the JobLog in the database
        List<JobLog> jobLogs = jobLogRepository.findAll();
        assertThat(jobLogs).hasSize(databaseSizeBeforeCreate + 1);
        JobLog testJobLog = jobLogs.get(jobLogs.size() - 1);
        assertThat(testJobLog.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testJobLog.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testJobLog.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testJobLog.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllJobLogs() throws Exception {
        // Initialize the database
        jobLogRepository.saveAndFlush(jobLog);

        // Get all the jobLogs
        restJobLogMockMvc.perform(get("/api/job-logs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jobLog.getId().intValue())))
                .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
                .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START_STR)))
                .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END_STR)))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getJobLog() throws Exception {
        // Initialize the database
        jobLogRepository.saveAndFlush(jobLog);

        // Get the jobLog
        restJobLogMockMvc.perform(get("/api/job-logs/{id}", jobLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jobLog.getId().intValue()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START_STR))
            .andExpect(jsonPath("$.end").value(DEFAULT_END_STR))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobLog() throws Exception {
        // Get the jobLog
        restJobLogMockMvc.perform(get("/api/job-logs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobLog() throws Exception {
        // Initialize the database
        jobLogService.save(jobLog);

        int databaseSizeBeforeUpdate = jobLogRepository.findAll().size();

        // Update the jobLog
        JobLog updatedJobLog = new JobLog();
        updatedJobLog.setId(jobLog.getId());
        updatedJobLog.setResult(UPDATED_RESULT);
        updatedJobLog.setStart(UPDATED_START);
        updatedJobLog.setEnd(UPDATED_END);
        updatedJobLog.setMessage(UPDATED_MESSAGE);

        restJobLogMockMvc.perform(put("/api/job-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedJobLog)))
                .andExpect(status().isOk());

        // Validate the JobLog in the database
        List<JobLog> jobLogs = jobLogRepository.findAll();
        assertThat(jobLogs).hasSize(databaseSizeBeforeUpdate);
        JobLog testJobLog = jobLogs.get(jobLogs.size() - 1);
        assertThat(testJobLog.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testJobLog.getStart()).isEqualTo(UPDATED_START);
        assertThat(testJobLog.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testJobLog.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void deleteJobLog() throws Exception {
        // Initialize the database
        jobLogService.save(jobLog);

        int databaseSizeBeforeDelete = jobLogRepository.findAll().size();

        // Get the jobLog
        restJobLogMockMvc.perform(delete("/api/job-logs/{id}", jobLog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<JobLog> jobLogs = jobLogRepository.findAll();
        assertThat(jobLogs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
