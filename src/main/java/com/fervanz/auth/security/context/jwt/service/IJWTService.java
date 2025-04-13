package com.fervanz.auth.security.context.jwt.service;

import com.fervanz.auth.security.context.jwt.dto.JWTResponse;
import com.fervanz.auth.security.models.entities.CustomRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

public interface IJWTService {
    JWTResponse generateToken(Long userId, long timeMillis, String type, String username, String name,
                              Set<CustomRole> authorities);

    boolean isAValidToken(String token);

    boolean validateRefreshToken(String token);

    Claims getClaims(String token);

    boolean isExpired(String token);

    String getUserIdFromToken(String token);

    String getUsernameFromToken(String token);

    String extractTokenFromRequest(HttpServletRequest request);
}
