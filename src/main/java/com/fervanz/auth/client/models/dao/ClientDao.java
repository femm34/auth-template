package com.fervanz.auth.client.models.dao;

import com.fervanz.auth.client.models.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientDao extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);

    Optional<Client> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}