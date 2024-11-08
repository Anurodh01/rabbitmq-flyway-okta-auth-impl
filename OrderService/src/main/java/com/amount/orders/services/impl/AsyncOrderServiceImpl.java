package com.amount.orders.services.impl;

import com.amount.orders.client.CustomerServiceWebClient;
import com.amount.orders.dto.CustomerDTO;
import com.amount.orders.dto.OrderDTO;
import com.amount.orders.enums.OrderStatus;
import com.amount.orders.exception.CustomException;
import com.amount.orders.exception.ErrorCode;
import com.amount.orders.exception.ResourceNotFoundException;
import com.amount.orders.models.Order;
import com.amount.orders.models.Product;
import com.amount.orders.repository.OrderRepository;
import com.amount.orders.services.OrderService;
import com.amount.orders.services.ProductService;
import com.amount.orders.util.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class AsyncOrderServiceImpl implements OrderService {
    private final RabbitTemplate rabbitTemplate;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final CustomerServiceWebClient customerServiceWebClient;
    private final Mapper mapper;
    public AsyncOrderServiceImpl(RabbitTemplate rabbitTemplate,
                                 OrderRepository orderRepository,
                                 ProductService productService,
                                 CustomerServiceWebClient customerServiceWebClient,
                                 Mapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.customerServiceWebClient = customerServiceWebClient;
        this.mapper = mapper;
    }
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.order.routing-key}")
    private String routingKey;
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        CustomerDTO customer = customerServiceWebClient.getCustomerDetails(orderDTO.getCustomerId());
        //validate productDetails
        Product product = productService.getProductById(orderDTO.getProductId());
        //validate the product Quantity
        if(product.getCount() < orderDTO.getQuantity()){
            throw new CustomException("Only "+ product.getCount()+" Item is Left!!");
        }

        if(!customer.isActive()){
            throw new CustomException(ErrorCode.INACTIVE_CUSTOMER);
        }
        //balance check
        if(customer.getWalletBalance() < (product.getCost() * orderDTO.getQuantity())){
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        Order order = mapper.dtoToOrder(orderDTO);
        productService.updateProductCount(order.getProductId(), -order.getQuantity());
        Order savedPendingOrder = orderRepository.save(order);
        OrderDTO savedOrder = mapper.orderToDTO(savedPendingOrder);

        //transaction on customer Service
        rabbitTemplate.convertAndSend(exchange, routingKey, savedOrder);

        return savedOrder;
    }

    @Override
    public OrderDTO getOrderById(int id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order with Id: " + id + " Not Found!"));
        return mapper.orderToDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOS = orders.stream().map(mapper::orderToDTO).toList();
        return orderDTOS;
    }

    @Override
    public OrderDTO cancelOrder(int id) {
        return null;
    }

    @Override
    public List<Order> getOrderByCustomerId(int id) {
        return null;
    }
    @Override
    public OrderDTO updateOrderStatus(int id, OrderStatus orderStatus) {
           Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Order Found with id: " + id));
           order.setStatus(orderStatus);
           Order updatedOrder = orderRepository.save(order);
           return mapper.orderToDTO(updatedOrder);
    }
}
