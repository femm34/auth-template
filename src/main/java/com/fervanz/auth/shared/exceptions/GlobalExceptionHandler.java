package com.fervanz.auth.shared.exceptions;

import com.fervanz.auth.shared.payload.GlobalResponse;
import com.fervanz.auth.shared.utils.ResponseGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<GlobalResponse> handleFinancialSystemException(GlobalException ex) {
        return ResponseGenerator.generateResponse(ex.getMessage(), null, ex.getHttpStatus(), ex.getHttpStatusCode().value());
    }
}
