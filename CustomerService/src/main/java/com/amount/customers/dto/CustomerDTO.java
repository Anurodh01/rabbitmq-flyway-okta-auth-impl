package com.amount.customers.dto;

import com.amount.customers.utils.CustomDoubleSerializer;
import com.amount.customers.utils.CustomIntegerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerDTO {
    private String id;
    @NotNull(message = "Email is required!")
    @NotBlank(message = "Email can not be blank!")
    @Email(message = "Please Enter a Valid Email")
    private String email;
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Atleast 8 Character, 1 uppercase, 1 digit and 1 special character!")
    private String password;
    @NotBlank(message = "Customer firstName can not be blank!")
    @NotNull(message = "Customer firstName is required!")
    private String firstName;
    @NotBlank(message = "Customer lastName can not be blank!")
    @NotNull(message = "Customer lastName is required!")
    private String lastName;

    @Min(value = 10, message = "Minimum Age should be 10!")
    @Max(value = 100, message = "Maximum Age should not exceed 100!")
    @JsonDeserialize(using = CustomIntegerSerializer.class)
    private Integer age;

    @Min(value = 0, message = "Invalid Amount!")
    @JsonDeserialize(using = CustomDoubleSerializer.class)
    private Double walletBalance;

    private boolean active = true;
}
