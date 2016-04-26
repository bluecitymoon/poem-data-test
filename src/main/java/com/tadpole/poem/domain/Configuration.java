package com.tadpole.poem.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Configuration.
 */
@Entity
@Table(name = "configuration")
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "content")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Configuration configuration = (Configuration) o;
        if(configuration.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, configuration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Configuration{" +
            "id=" + id +
            ", identifier='" + identifier + "'" +
            ", content='" + content + "'" +
            '}';
    }
}
