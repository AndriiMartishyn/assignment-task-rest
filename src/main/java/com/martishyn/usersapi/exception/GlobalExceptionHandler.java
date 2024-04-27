package com.martishyn.usersapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<Object> handleApiException(ApiErrorException apiErrorException){
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(apiErrorException.getStatusCode().value(), apiErrorException.getReason());
        return buildResponseEntity(apiExceptionResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiExceptionResponse apiExceptionResponse) {
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.getHttpStatus());
    }
}
