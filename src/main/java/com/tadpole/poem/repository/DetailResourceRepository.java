package com.tadpole.poem.repository;

import com.tadpole.poem.domain.DetailResource;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DetailResource entity.
 */
public interface DetailResourceRepository extends JpaRepository<DetailResource,Long> {

    List<DetailResource> findByVisitCountIsNull();

    DetailResource findByUrl(String url);

}
