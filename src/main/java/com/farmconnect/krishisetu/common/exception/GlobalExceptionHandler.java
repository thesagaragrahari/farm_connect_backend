package com.farmconnect.krishisetu.common.exception;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CentralizedException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(CentralizedException ex) {
        ErrorDefinition def = ex.getErrorDefinition();
        
        ApiErrorResponse response = new ApiErrorResponse(
            def.getCode(),
            ex.getMessage(), // Formatted message
            System.currentTimeMillis(),
            UUID.randomUUID().toString() // Useful for log tracking
        );

        return ResponseEntity
                .status(def.getStatus())
                .body(response);
    }
}