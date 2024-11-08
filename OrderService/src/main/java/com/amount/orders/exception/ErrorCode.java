package com.amount.orders.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INACTIVE_CUSTOMER("Customer is InActive!!"),
    INVALID_OPERATION("Invalid Operation!"),
    INSUFFICIENT_BALANCE("InSufficient Balance to Place this Order!!");
    private String message;
    ErrorCode(String message){
        this.message = message;
    }
}
