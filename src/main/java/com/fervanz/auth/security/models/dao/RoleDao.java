package com.fervanz.auth.security.models.dao;

import com.fervanz.auth.security.models.entities.CustomRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDao extends JpaRepository<CustomRole, Long> {

    @Query(value = "SELECT * FROM role_entity WHERE name = ?1", nativeQuery = true)
    Optional<CustomRole> findByNameString(String name);

}
