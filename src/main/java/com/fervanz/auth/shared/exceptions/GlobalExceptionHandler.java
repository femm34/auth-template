package com.fervanz.auth.shared.exceptions;

import com.fervanz.auth.shared.payload.GlobalResponse;
import com.fervanz.auth.shared.utils.ResponseGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<GlobalResponse> handleFinancialSystemException(GlobalException ex) {
        return ResponseGenerator.generateResponse(ex.getMessage(), null, ex.getHttpStatus(), ex.getHttpStatusCode().value());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalResponse> handleEnumParsingException(HttpMessageNotReadableException ex) {
        return ResponseGenerator.generateResponse(
                ex.getMessage(),
                null,
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
    }

    private String extractMessageFromException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .findFirst()
                .orElse("Error de validación desconocido");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse> handleValidationException(MethodArgumentNotValidException ex) {

        return ResponseGenerator.generateResponse(
                extractMessageFromException(ex),
                null,
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
    }

}
