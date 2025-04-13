package com.fervanz.auth.security.context.jwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class JWTResponse {
    private String token;
}
