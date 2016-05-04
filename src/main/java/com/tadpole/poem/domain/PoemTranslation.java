package com.tadpole.poem.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PoemTranslation.
 */
@Entity
@Table(name = "poem_translation")
public class PoemTranslation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 20000)
    @Column(name = "translation", length = 20000)
    private String translation;

    @Column(name = "poem_id")
    private Long poemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Long getPoemId() {
        return poemId;
    }

    public void setPoemId(Long poemId) {
        this.poemId = poemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PoemTranslation poemTranslation = (PoemTranslation) o;
        if(poemTranslation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, poemTranslation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PoemTranslation{" +
            "id=" + id +
            ", translation='" + translation + "'" +
            ", poemId='" + poemId + "'" +
            '}';
    }
}
