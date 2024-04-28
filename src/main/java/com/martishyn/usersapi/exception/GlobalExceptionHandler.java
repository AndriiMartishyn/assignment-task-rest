package com.martishyn.usersapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<Object> handleApiException(ApiErrorException apiErrorException) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.valueOf(apiErrorException.getStatusCode().value()));
        apiErrorDto.setMessage(apiErrorException.getReason());
        return buildResponseEntity(apiErrorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleNotValidMethodArgument(MethodArgumentNotValidException exception) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST);
        apiErrorDto.setMessage("Validation Error");
        apiErrorDto.addValidationErrors(exception.getBindingResult().getFieldErrors());
        return buildResponseEntity(apiErrorDto);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorDto apiErrorDto) {
        return new ResponseEntity<>(apiErrorDto, apiErrorDto.getHttpStatus());
    }
}
