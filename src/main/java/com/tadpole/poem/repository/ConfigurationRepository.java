package com.tadpole.poem.repository;

import com.tadpole.poem.domain.Configuration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Configuration entity.
 */
public interface ConfigurationRepository extends JpaRepository<Configuration,Long> {

    Configuration findByIdentifier(String identifier);
}
