package com.tadpole.poem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "description")
    private String description;

    @Column(name = "target")
    private String target;

    @Column(name = "last_start")
    private ZonedDateTime lastStart;

    @Column(name = "last_stop")
    private ZonedDateTime lastStop;

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private Set<JobLog> jobLogs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ZonedDateTime getLastStart() {
        return lastStart;
    }

    public void setLastStart(ZonedDateTime lastStart) {
        this.lastStart = lastStart;
    }

    public ZonedDateTime getLastStop() {
        return lastStop;
    }

    public void setLastStop(ZonedDateTime lastStop) {
        this.lastStop = lastStop;
    }

    public Set<JobLog> getJobLogs() {
        return jobLogs;
    }

    public void setJobLogs(Set<JobLog> jobLogs) {
        this.jobLogs = jobLogs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        if(job.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", identifier='" + identifier + "'" +
            ", locked='" + locked + "'" +
            ", description='" + description + "'" +
            ", target='" + target + "'" +
            ", lastStart='" + lastStart + "'" +
            ", lastStop='" + lastStop + "'" +
            '}';
    }
}
