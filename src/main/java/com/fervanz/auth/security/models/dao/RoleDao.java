package com.fervanz.auth.security.models.dao;

import com.fervanz.auth.security.models.entities.CustomRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<CustomRole, Long> {

}
