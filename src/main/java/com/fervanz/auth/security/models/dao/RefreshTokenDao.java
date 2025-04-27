package com.fervanz.auth.security.models.dao;

import com.fervanz.auth.client.models.entities.Client;
import com.fervanz.auth.security.models.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByClient(Client client);
}
