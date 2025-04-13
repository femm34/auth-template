package com.fervanz.auth.shared.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class GlobalException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final HttpStatusCode httpStatusCode;

    public GlobalException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.httpStatusCode = HttpStatus.BAD_REQUEST;
    }
}
