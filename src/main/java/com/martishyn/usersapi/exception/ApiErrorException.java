package com.martishyn.usersapi.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ApiErrorException extends ResponseStatusException {

    public ApiErrorException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
