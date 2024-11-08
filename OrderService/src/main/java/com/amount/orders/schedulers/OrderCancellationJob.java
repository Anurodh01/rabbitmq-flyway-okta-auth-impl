package com.amount.orders.schedulers;

import com.amount.orders.enums.OrderStatus;
import com.amount.orders.models.Order;
import com.amount.orders.repository.OrderRepository;
import com.amount.orders.services.OrderService;
import com.amount.orders.services.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class OrderCancellationJob {
    private ProductService productService;
    private OrderRepository orderRepository;
    private OrderService orderService;
    @Scheduled(cron = "0 */2 * * * *")
    public void deleteThePendingOrders(){
        //get the all pending orders which has creation time more than 1 min and still in pending state
        List<Order> orders = orderRepository.findAll();
        log.info("Job Executed: {}", LocalDateTime.now());
        orders.stream().filter(order-> order.getStatus().equals(OrderStatus.PENDING)).forEach(order-> {
            if(Duration.between(order.getOrderDate(), LocalDateTime.now()).toMinutes() > 1){
                //order status update
                orderService.updateOrderStatus(order.getId(),OrderStatus.CANCELLED);
                //update the product count
                productService.updateProductCount(order.getProductId(), order.getQuantity());
            }
        });
    }
}
