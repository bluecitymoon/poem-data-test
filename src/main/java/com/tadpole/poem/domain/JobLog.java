package com.tadpole.poem.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.tadpole.poem.domain.enumeration.JobExecutionResult;

/**
 * A JobLog.
 */
@Entity
@Table(name = "job_log")
public class JobLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private JobExecutionResult result;

    @Column(name = "start")
    private ZonedDateTime start;

    @Column(name = "end")
    private ZonedDateTime end;

    @Column(name = "message")
    private String message;

    @ManyToOne
    private Job job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobExecutionResult getResult() {
        return result;
    }

    public void setResult(JobExecutionResult result) {
        this.result = result;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobLog jobLog = (JobLog) o;
        if(jobLog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jobLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobLog{" +
            "id=" + id +
            ", result='" + result + "'" +
            ", start='" + start + "'" +
            ", end='" + end + "'" +
            ", message='" + message + "'" +
            '}';
    }
}
