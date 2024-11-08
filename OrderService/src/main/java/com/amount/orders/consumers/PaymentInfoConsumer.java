package com.amount.orders.consumers;

import com.amount.orders.dto.OrderDTO;
import com.amount.orders.dto.PaymentInfoDTO;
import com.amount.orders.enums.OrderStatus;
import com.amount.orders.exception.CustomException;
import com.amount.orders.services.OrderService;
import com.amount.orders.services.ProductService;
import com.amount.orders.util.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
@Slf4j
public class PaymentInfoConsumer {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @RabbitListener(queues = {"${rabbitmq.payment.queue}"})
    public void paymentInfoConsumer(PaymentInfoDTO paymentInfoDTO){
      log.info("Payment Info Received: {}", Log.requestResponseData(paymentInfoDTO));
      OrderDTO order = paymentInfoDTO.getOrder();
      if(paymentInfoDTO.isPaymentStatus()){
          orderService.updateOrderStatus(order.getId(), OrderStatus.CONFIRMED);
          log.info("Payment of Rs: {} Successful for OrderId: {}", order.getTotalPrice(), order.getId());
      } else {
          log.error("Payment Failed For OrderId: {}", order.getId());

          //if payment failed, cancel the order
          orderService.cancelOrder(order.getId());
          //update the product count
          productService.updateProductCount(order.getProductId(), order.getQuantity());
      }
    }
}
