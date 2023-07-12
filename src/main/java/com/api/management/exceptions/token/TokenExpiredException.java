package com.api.management.exceptions.token;

import org.springframework.http.HttpStatus;

import com.api.management.exceptions.handler.IGlobalExceptionStatusMapping;

public class TokenExpiredException extends RuntimeException implements IGlobalExceptionStatusMapping {

    public TokenExpiredException() {
        super("Expired token");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
