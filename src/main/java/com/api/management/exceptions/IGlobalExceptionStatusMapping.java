package com.api.management.exceptions;

import org.springframework.http.HttpStatus;

public interface IGlobalExceptionStatusMapping {
    HttpStatus getStatus();
}