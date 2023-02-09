package com.bitcoin.interview.service.exception;

public class FailedToCreatePaymentException extends RuntimeException {
    public FailedToCreatePaymentException(String message) {
        super(message);
    }
}
