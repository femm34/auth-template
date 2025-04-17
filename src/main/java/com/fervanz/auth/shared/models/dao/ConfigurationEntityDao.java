package com.fervanz.auth.shared.models.dao;

import com.fervanz.auth.shared.models.entities.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationEntityDao extends JpaRepository<ConfigurationEntity, Long> {
    Optional<ConfigurationEntity> findByKey(String key);
}
