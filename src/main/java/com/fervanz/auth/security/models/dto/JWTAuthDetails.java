package com.fervanz.auth.security.models.dto;

import lombok.*;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Builder
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class JWTAuthDetails {
    private final WebAuthenticationDetails webAuthenticationDetailsSource;
    private final String jwtToken;
}


