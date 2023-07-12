package com.api.management.exceptions.user;

import org.springframework.http.HttpStatus;

import com.api.management.exceptions.handler.IGlobalExceptionStatusMapping;

public class UserNotFoundException extends RuntimeException implements IGlobalExceptionStatusMapping {

    public UserNotFoundException() {
        super("User not found");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
