package com.amount.orders.controllers;

import com.amount.orders.dto.OrderDTO;
import com.amount.orders.services.OrderService;
import com.amount.orders.util.Log;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @PostMapping
    public ResponseEntity<EntityModel<OrderDTO>> createOrder(@Valid @RequestBody OrderDTO orderDTO){
        log.info("Create Order Request Data: {}", Log.requestResponseData(orderDTO));
        OrderDTO createdOrderDTO = orderService.createOrder(orderDTO);
        log.info("Order created with Identifier: {}, Response Data: {}", createdOrderDTO.getId(), Log.requestResponseData(createdOrderDTO));
        EntityModel<OrderDTO> modelDTO = EntityModel.of(createdOrderDTO,
                linkTo(methodOn(OrderController.class).getOrderById(createdOrderDTO.getId())).withRel("Check Latest Status")
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(modelDTO);
    }
    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable int id){
        OrderDTO orderDTO = orderService.cancelOrder(id);
        log.info("Order with identifier: {}, Cancelled! Response Data: {}", orderDTO.getId(), Log.requestResponseData(orderDTO));
        return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
    }

}
