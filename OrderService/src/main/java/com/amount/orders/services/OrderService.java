package com.amount.orders.services;

import com.amount.orders.dto.OrderDTO;
import com.amount.orders.enums.OrderStatus;
import com.amount.orders.models.Order;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(int id);
    List<OrderDTO> getAllOrders();
    OrderDTO cancelOrder(int id);
    OrderDTO updateOrderStatus(int id, OrderStatus orderStatus);
    List<Order> getOrderByCustomerId(int id);
}
