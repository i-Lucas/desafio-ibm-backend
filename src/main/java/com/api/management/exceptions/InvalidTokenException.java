package com.api.management.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends RuntimeException implements IGlobalExceptionStatusMapping {

    public InvalidTokenException() {
        super("Invalid Token.");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
