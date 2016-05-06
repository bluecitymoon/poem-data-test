package com.tadpole.poem.repository;

import com.tadpole.poem.domain.Poem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Poem entity.
 */
public interface PoemRepository extends JpaRepository<Poem,Long> {

    Poem findByResourceId(String resourceId);

}
