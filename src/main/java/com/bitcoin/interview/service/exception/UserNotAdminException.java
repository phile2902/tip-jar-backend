package com.bitcoin.interview.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserNotAdminException extends RuntimeException {
    public UserNotAdminException(String message) {
        super(message);
    }
}
