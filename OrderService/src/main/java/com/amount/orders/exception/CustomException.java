package com.amount.orders.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private String message;
    public CustomException(ErrorCode errorCode){
        this.message = errorCode.getMessage();
    }
    public CustomException(String message){
        super(message);
        this.message = message;
    }
}
