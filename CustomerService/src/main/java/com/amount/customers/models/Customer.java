package com.amount.customers.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "customers")
public class Customer {
    @Id
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private int age;
    private double walletBalance;

    private boolean active = true;
}
