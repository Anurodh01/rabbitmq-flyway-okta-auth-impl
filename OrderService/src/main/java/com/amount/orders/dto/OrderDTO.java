package com.amount.orders.dto;

import com.amount.orders.enums.OrderStatus;
import com.amount.orders.models.Product;
import com.amount.orders.util.CustomIntegerSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int id;
    @NotNull(message = "CustomerId is Required!!")
    private String customerId;

    @NotNull(message = "ProductId is required!!")
    @JsonDeserialize(using = CustomIntegerSerializer.class)
    private Integer productId;
    @NotNull(message = "Please Provide the Quantity!!")
    @JsonDeserialize(using = CustomIntegerSerializer.class)
    private Integer quantity;
    private CustomerDTO customerDetails;
    private Product productDetails;
    private OrderStatus status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String order_date;
    private Double totalPrice;
}
