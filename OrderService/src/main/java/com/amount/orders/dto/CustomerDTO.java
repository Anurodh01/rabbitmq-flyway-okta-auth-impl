package com.amount.orders.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int age;
    private double walletBalance;
    private boolean active;
}