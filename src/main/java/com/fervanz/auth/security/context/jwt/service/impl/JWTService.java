package com.fervanz.auth.security.context.jwt.service.impl;

import com.fervanz.auth.security.context.jwt.dto.JWTResponse;
import com.fervanz.auth.security.context.jwt.enums.TokenType;
import com.fervanz.auth.security.context.jwt.service.IJWTService;
import com.fervanz.auth.security.models.entities.CustomRole;
import com.fervanz.auth.security.models.mapper.RoleMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Set;


@Service
public class JWTService implements IJWTService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    @Override
    public String generateRefreshToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    @Override
    public JWTResponse generateToken(Long userId, long expiryTime, TokenType type, String username, String name,
                                     Set<CustomRole> authorities) {
        return JWTResponse.builder().token(Jwts.builder()
                .setIssuer(jwtIssuer)
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .claim("type", type.name())
                .claim("username", username)
                .claim("name", name)
                .claim("authorities", RoleMapper.toString(authorities))
                .signWith(getSigningKey())
                .compact()).build();
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    @Override
    public boolean isTokenAccess(String token) {
        try {
            return TokenType.ACCESS_TOKEN.name().equals(getClaims(token).get("type", String.class));
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean isResetPasswordToken(String token) {
        try {
            return TokenType.RESET_PASSWORD.name().equals(getClaims(token).get("type", String.class));
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isExpired(String token) {
        try {
            return this.getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(this.jwtSecret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("sub", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            return getClaims(token).get("username", String.class);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "access_token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
