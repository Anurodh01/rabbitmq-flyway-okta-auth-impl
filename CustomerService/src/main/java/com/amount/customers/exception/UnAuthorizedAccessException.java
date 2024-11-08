package com.amount.customers.exception;

public class UnAuthorizedAccessException extends RuntimeException {
    public UnAuthorizedAccessException(String s) {
        super(s);
    }
}
