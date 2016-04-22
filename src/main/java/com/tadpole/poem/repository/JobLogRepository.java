package com.tadpole.poem.repository;

import com.tadpole.poem.domain.JobLog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobLog entity.
 */
public interface JobLogRepository extends JpaRepository<JobLog,Long> {

}
