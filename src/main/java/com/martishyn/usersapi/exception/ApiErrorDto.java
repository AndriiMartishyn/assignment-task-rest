package com.martishyn.usersapi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiErrorDto {
    private final HttpStatus httpStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private List<ApiSubErrorDto> subErrors;

    public ApiErrorDto(HttpStatus status) {
        this.httpStatus = status;
        this.timestamp = LocalDateTime.now();
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        if (subErrors == null){
            subErrors = new ArrayList<>();
        }
        fieldErrors.forEach(this::createSubErrorFrom);
    }

    private void createSubErrorFrom(FieldError fieldError) {
        subErrors.add(new ApiSubErrorDto(fieldError.getObjectName(),fieldError.getField(),
                fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ApiSubErrorDto> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<ApiSubErrorDto> subErrors) {
        this.subErrors = subErrors;
    }
}
