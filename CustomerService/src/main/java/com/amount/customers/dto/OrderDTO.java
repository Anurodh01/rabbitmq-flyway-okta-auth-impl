package com.amount.customers.dto;

import com.amount.customers.utils.CustomIntegerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int id;
    @NotNull(message = "CustomerId is Required!!")
    @JsonDeserialize(using = CustomIntegerSerializer.class)
    private String customerId;
    private CustomerDTO customerDetails;
    @NotNull(message = "ProductId is required!!")
    @JsonDeserialize(using = CustomIntegerSerializer.class)
    private Integer productId;
    @NotNull(message = "Please Provide the Quantity!!")
    @JsonDeserialize(using = CustomIntegerSerializer.class)
    private Integer quantity;
    private String order_date;
    private Double totalPrice;
}
