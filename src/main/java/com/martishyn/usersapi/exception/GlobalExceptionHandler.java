package com.martishyn.usersapi.exception;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<Object> handleApiException(ApiErrorException apiErrorException) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.valueOf(apiErrorException.getStatusCode().value()));
        apiErrorDto.setMessage(apiErrorException.getReason());
        return buildResponseEntity(apiErrorDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleWrongPassedParameter(MethodArgumentTypeMismatchException exception) {
        String error = "Wrong input parameters in URL";
        return buildResponseEntity(new ApiErrorDto(HttpStatus.BAD_REQUEST, error, exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleNotValidMethodArgument(MethodArgumentNotValidException exception) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST);
        apiErrorDto.setMessage("Validation Error");
        apiErrorDto.addValidationErrors(exception.getBindingResult().getFieldErrors());
        return buildResponseEntity(apiErrorDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST);
        apiErrorDto.setMessage("Validation Error");
        apiErrorDto.addValidationErrors(exception.getConstraintViolations());
        return buildResponseEntity(apiErrorDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleNotValidJsonInput(HttpMessageNotReadableException exception) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiErrorDto(HttpStatus.BAD_REQUEST, error, exception));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorDto apiErrorDto) {
        return new ResponseEntity<>(apiErrorDto, apiErrorDto.getHttpStatus());
    }
}
