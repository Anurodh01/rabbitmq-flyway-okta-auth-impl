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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Primary
public class OrderServiceImpl implements OrderService {
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final Mapper mapper;
    private final CustomerServiceWebClient customerServiceClient;
    @Autowired
    public OrderServiceImpl(CustomerServiceWebClient customerServiceClient, ProductService productService, OrderRepository orderRepository, Mapper mapper) {
        this.customerServiceClient = customerServiceClient;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        //validate CustomerDetails
        CustomerDTO customer = customerServiceClient.getCustomerDetails(orderDTO.getCustomerId());
        //validate productDetails
        Product product = productService.getProductById(orderDTO.getProductId());
        //validate the product Quantity
        if(product.getCount() < orderDTO.getQuantity()){
            throw new CustomException("Only "+ product.getCount()+" Item is Left!!");
        }
        //balance check
        if(customer.getWalletBalance() < (product.getCost() * orderDTO.getQuantity())){
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        Order order = mapper.dtoToOrder(orderDTO);
        productService.updateProductCount(order.getProductId(), -order.getQuantity());
        Order savedPendingOrder = orderRepository.save(order);

        //Money Transaction Started
        if(!customer.isActive()){
            throw new CustomException(ErrorCode.INACTIVE_CUSTOMER);
        }
        //deduct money from wallet and place order
        customer.setWalletBalance(customer.getWalletBalance() - (product.getCost() * order.getQuantity()));
        customerServiceClient.updateCustomerWallet(customer);

        //update the status of order
        savedPendingOrder.setStatus(OrderStatus.CONFIRMED);
        Order confirmedOrder = orderRepository.save(savedPendingOrder);
        return mapper.orderToDTO(confirmedOrder);
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
    public OrderDTO cancelOrder(int id){
        Order order = orderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Order found with id: "+ id));

        if(order.getStatus().equals(OrderStatus.PENDING)){
            order.setStatus(OrderStatus.CANCELLED);
        } else if(order.getStatus().equals(OrderStatus.CONFIRMED)) {
            //update the customer wallet
            CustomerDTO customer = customerServiceClient.getCustomerDetails(order.getCustomerId());
            Product product = productService.getProductById(order.getProductId());

            customer.setWalletBalance(customer.getWalletBalance() + (product.getCost() * order.getQuantity()));
            customerServiceClient.updateCustomerWallet(customer);
            //update the status
            order.setStatus(OrderStatus.CANCELLED);
        }else {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }
        //update the product quantity
        productService.updateProductCount(order.getProductId(), order.getQuantity());
        Order cancelledOrder = orderRepository.save(order);
        return mapper.orderToDTO(cancelledOrder);
    }
    @Override
    public OrderDTO updateOrderStatus(int id, OrderStatus orderStatus){
         Order order = orderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Order Found with id: "+ id));
         order.setStatus(orderStatus);
         Order updatedOrder = orderRepository.save(order);
         return mapper.orderToDTO(updatedOrder);
    }
    @Override
    public List<Order> getOrderByCustomerId(int id) {
        return null;
    }
}
