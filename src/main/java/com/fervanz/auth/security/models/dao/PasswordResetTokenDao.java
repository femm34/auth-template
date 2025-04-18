package com.fervanz.auth.security.models.dao;

import com.fervanz.auth.security.models.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenDao extends JpaRepository<PasswordResetToken, Long> {
}
