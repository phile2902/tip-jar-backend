package com.bitcoin.interview.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AuthNotFoundException extends RuntimeException {
    public AuthNotFoundException(String message) {
        super(message);
    }
}