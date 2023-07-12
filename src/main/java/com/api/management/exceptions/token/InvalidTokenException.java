package com.api.management.exceptions.token;

import org.springframework.http.HttpStatus;

import com.api.management.exceptions.handler.IGlobalExceptionStatusMapping;

public class InvalidTokenException extends RuntimeException implements IGlobalExceptionStatusMapping {

    public InvalidTokenException() {
        super("Invalid Token.");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
