package com.api.management.exceptions.handler;

import org.springframework.http.HttpStatus;

public interface IGlobalExceptionStatusMapping {
    HttpStatus getStatus();
}