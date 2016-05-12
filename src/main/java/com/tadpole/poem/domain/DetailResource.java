package com.tadpole.poem.domain;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DetailResource.
 */
@Entity
@Table(name = "detail_resource")
public class DetailResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "outside_id")
    private String outsideId;

    @Column(name = "visit_count")
    private Integer visitCount;

    @Column(name = "active")
    private Boolean active;

    @Size(max = 1000)
    @Column(name = "title")
    private String title;

    @Column(name = "tag")
    private String tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOutsideId() {
        return outsideId;
    }

    public void setOutsideId(String outsideId) {
        this.outsideId = outsideId;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DetailResource detailResource = (DetailResource) o;
        if(detailResource.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, detailResource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DetailResource{" +
            "id=" + id +
            ", url='" + url + "'" +
            ", outsideId='" + outsideId + "'" +
            ", visitCount='" + visitCount + "'" +
            ", active='" + active + "'" +
            ", title='" + title + "'" +
            ", tag='" + tag + "'" +
            '}';
    }
}
