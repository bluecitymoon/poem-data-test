package com.tadpole.poem.repository;

import com.tadpole.poem.domain.PoemTranslation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PoemTranslation entity.
 */
public interface PoemTranslationRepository extends JpaRepository<PoemTranslation,Long> {

}
