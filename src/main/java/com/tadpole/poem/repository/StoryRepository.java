package com.tadpole.poem.repository;

import com.tadpole.poem.domain.Story;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Story entity.
 */
public interface StoryRepository extends JpaRepository<Story,Long> {

}
