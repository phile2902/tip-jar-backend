package com.bitcoin.interview.service.exception;

public class NotAllowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotAllowedException(String msg) {
        super(msg);
    }
}
