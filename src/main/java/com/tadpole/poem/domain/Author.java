package com.tadpole.poem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Author.
 */
@Entity
@Table(name = "author")
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_year")
    private String birthYear;

    @Column(name = "die_year")
    private String dieYear;

    @Column(name = "zi")
    private String zi;

    @Column(name = "hao")
    private String hao;

    @Column(name = "avatar_file_name")
    private String avatarFileName;

    @Column(name = "link")
    private String link;

    @Size(max = 5000)
    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "visit_count")
    private Integer visitCount;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private Set<Poem> poems = new HashSet<>();

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

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getDieYear() {
        return dieYear;
    }

    public void setDieYear(String dieYear) {
        this.dieYear = dieYear;
    }

    public String getZi() {
        return zi;
    }

    public void setZi(String zi) {
        this.zi = zi;
    }

    public String getHao() {
        return hao;
    }

    public void setHao(String hao) {
        this.hao = hao;
    }

    public String getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Set<Poem> getPoems() {
        return poems;
    }

    public void setPoems(Set<Poem> poems) {
        this.poems = poems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Author author = (Author) o;
        if(author.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Author{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", birthYear='" + birthYear + "'" +
            ", dieYear='" + dieYear + "'" +
            ", zi='" + zi + "'" +
            ", hao='" + hao + "'" +
            ", avatarFileName='" + avatarFileName + "'" +
            ", link='" + link + "'" +
            ", description='" + description + "'" +
            ", visitCount='" + visitCount + "'" +
            '}';
    }
}
