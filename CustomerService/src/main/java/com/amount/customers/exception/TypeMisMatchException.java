package com.amount.customers.exception;

public class TypeMisMatchException extends RuntimeException {
    public TypeMisMatchException(String errorMessage) {
        super(errorMessage);
    }
}
