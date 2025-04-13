package com.fervanz.auth.shared.utils;

import com.fervanz.auth.shared.payload.GlobalResponse;
import org.springframework.http.ResponseEntity;

public abstract class ResponseGenerator {
    public static ResponseEntity<GlobalResponse> generateResponse(String message, Object data, Object status, int code) {
        return ResponseEntity.ok(GlobalResponse.builder()
                .message(message)
                .status(status)
                .code(code)
                .data(data)
                .build());
    }
}
