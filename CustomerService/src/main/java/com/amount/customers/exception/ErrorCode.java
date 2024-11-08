package com.amount.customers.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    CUSTOMER_ALREADY_EXIST("Customer Email Already Exist!");
    private String message;
    ErrorCode(String message){
        this.message = message;
    }
}
