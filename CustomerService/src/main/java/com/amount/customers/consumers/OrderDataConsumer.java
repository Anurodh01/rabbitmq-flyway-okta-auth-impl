package com.amount.customers.consumers;

import com.amount.customers.dto.CustomerDTO;
import com.amount.customers.dto.OrderDTO;
import com.amount.customers.dto.PaymentInfoDTO;
import com.amount.customers.services.CustomerService;
import com.amount.customers.utils.Log;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderDataConsumer {
    @Autowired
    public CustomerService customerService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.payment.queue}")
    private String paymentQueue;
    @RabbitListener(queues = {"${rabbitmq.order.queue}"})
    public void orderDataConsumer(OrderDTO orderDTO){
      log.info("OrderData consumed: {}", Log.requestResponseData(orderDTO));
      PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();
      try{
          CustomerDTO customer = customerService.getCustomerById(orderDTO.getCustomerId());;
          double totalAmount =  orderDTO.getTotalPrice();
          customer.setWalletBalance(customer.getWalletBalance() - totalAmount);
          CustomerDTO customerDTO = customerService.updateCustomer(customer, customer.getId());
          orderDTO.setCustomerDetails(customerDTO);

          //Payment update for the Order Service
          paymentInfoDTO.setOrder(orderDTO);
          paymentInfoDTO.setPaymentStatus(true);

          rabbitTemplate.convertAndSend(exchange, paymentQueue, paymentInfoDTO);

      } catch(Exception ex){
          log.error(ex.getLocalizedMessage());
          paymentInfoDTO.setOrder(orderDTO);
          paymentInfoDTO.setPaymentStatus(false);

          rabbitTemplate.convertAndSend(exchange, paymentQueue, paymentInfoDTO);
      }
    }
}
