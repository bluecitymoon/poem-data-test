package com.tadpole.poem.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Poem.
 */
@Entity
@Table(name = "poem")
public class Poem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "anthor_name")
    private String anthorName;

    @Column(name = "title")
    private String title;

    @Size(max = 10000)
    @Column(name = "content", length = 10000)
    private String content;

    @Column(name = "year")
    private String year;

    @ManyToOne
    private Author author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnthorName() {
        return anthorName;
    }

    public void setAnthorName(String anthorName) {
        this.anthorName = anthorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Poem poem = (Poem) o;
        if(poem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, poem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Poem{" +
            "id=" + id +
            ", anthorName='" + anthorName + "'" +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", year='" + year + "'" +
            '}';
    }
}
