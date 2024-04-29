package com.martishyn.usersapi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JsonRootName(value = "data")
public class ApiErrorDto {
    private HttpStatus httpStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String debugMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ApiSubErrorDto> subErrors;

    private ApiErrorDto() {
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

    public void addSubError(ApiSubErrorDto apiSubErrorDto) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(apiSubErrorDto);
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.stream()
                .map(this::creaseSubErrorFromFieldError)
                .forEach(this::addSubError);
    }

    private ApiSubErrorDto creaseSubErrorFromFieldError(FieldError fieldError) {
        return new ApiSubErrorDto(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.stream()
                .map(this::creaseSubErrorFromViolation)
                .forEach(this::addSubError);
    }

    private ApiSubErrorDto creaseSubErrorFromViolation(ConstraintViolation<?> violation) {
        return new ApiSubErrorDto(((PathImpl) violation.getPropertyPath()).getLeafNode().asString(),
                violation.getInvalidValue(), violation.getMessage());
    }

    private void addValidationError(String string, Object invalidValue, String message) {
        subErrors.add(new ApiSubErrorDto(string, invalidValue, message));
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
