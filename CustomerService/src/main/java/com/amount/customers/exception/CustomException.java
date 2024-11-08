package com.amount.customers.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {
    private String message;
    public CustomException(ErrorCode errorCode){
        this.message = errorCode.getMessage();
    }
    public CustomException(String message){
        this.message = message;
    }
}
