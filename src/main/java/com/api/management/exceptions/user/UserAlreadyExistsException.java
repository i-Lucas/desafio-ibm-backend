package com.api.management.exceptions.user;

import org.springframework.http.HttpStatus;

import com.api.management.exceptions.handler.IGlobalExceptionStatusMapping;

public class UserAlreadyExistsException extends RuntimeException implements IGlobalExceptionStatusMapping {

    public UserAlreadyExistsException() {
        super("E-mail already exists");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
