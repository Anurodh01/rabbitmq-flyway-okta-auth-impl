package com.amount.orders.util;

import com.amount.orders.client.CustomerServiceWebClient;
import com.amount.orders.dto.CustomerDTO;
import com.amount.orders.dto.OrderDTO;
import com.amount.orders.models.Order;
import com.amount.orders.models.Product;
import com.amount.orders.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    @Autowired
    private CustomerServiceWebClient customerServiceWebClient;
    @Autowired
    private ProductService productService;
    public OrderDTO orderToDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setCustomerId(order.getCustomerId());
        orderDTO.setProductId(order.getProductId());
        Product product = productService.getProductById(order.getProductId());
        orderDTO.setProductDetails(product);
        orderDTO.setCustomerDetails(customerServiceWebClient.getCustomerDetails(order.getCustomerId()));
        orderDTO.setOrder_date(order.getOrderDate().toString());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setTotalPrice(product.getCost() *  order.getQuantity());
        return orderDTO;
    }
    public Order dtoToOrder(OrderDTO dto){
        Order order = new Order();
        order.setId(dto.getId());
        order.setCustomerId(dto.getCustomerId());
        order.setProductId(dto.getProductId());
        order.setQuantity(dto.getQuantity());
        return order;
    }
}
