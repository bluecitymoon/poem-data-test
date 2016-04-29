package com.tadpole.poem.repository;

import com.tadpole.poem.domain.Tag;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tag entity.
 */
public interface TagRepository extends JpaRepository<Tag,Long> {

    Tag findByIdentifier(String identifier);
}
