package com.martishyn.usersapi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiErrorDto {
    private HttpStatus httpStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ApiSubErrorDto> subErrors;

    private ApiErrorDto(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorDto(HttpStatus status) {
        this();
        this.httpStatus = status;
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorDto(HttpStatus status, String message, Throwable ex) {
        this();
        this.httpStatus = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        if (subErrors == null){
            subErrors = new ArrayList<>();
        }
        fieldErrors.forEach(this::createSubErrorFrom);
    }

    private void createSubErrorFrom(FieldError fieldError) {
        subErrors.add(new ApiSubErrorDto(fieldError.getObjectName(),fieldError.getField(), fieldError.getDefaultMessage()));
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

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public List<ApiSubErrorDto> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<ApiSubErrorDto> subErrors) {
        this.subErrors = subErrors;
    }
}
