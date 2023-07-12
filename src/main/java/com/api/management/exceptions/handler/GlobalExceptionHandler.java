package com.api.management.exceptions.handler;

import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> responseBody = new HashMap<>();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception exception) {

        HttpStatus status = this.getStatus(exception);
        responseBody.putAll(Map.of("message", exception.getMessage(), "status", status));
        return new ResponseEntity<>(responseBody, status);
    }

    private HttpStatus getStatus(Exception exception) {

        if (exception instanceof IGlobalExceptionStatusMapping)
            return ((IGlobalExceptionStatusMapping) exception).getStatus();

        if (exception instanceof MethodArgumentNotValidException)
            return HttpStatus.BAD_REQUEST;

        return HttpStatus.INTERNAL_SERVER_ERROR; // não mapeado
    }
}