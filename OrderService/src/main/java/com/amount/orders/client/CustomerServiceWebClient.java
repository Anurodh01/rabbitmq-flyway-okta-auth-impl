package com.amount.orders.client;

import com.amount.orders.dto.CustomerDTO;
import com.amount.orders.exception.ResourceNotFoundException;
import com.amount.orders.util.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomerServiceWebClient {
    private final WebClient webClient;
    public CustomerServiceWebClient(WebClient webClient){
        this.webClient = webClient;
    }
    public CustomerDTO getCustomerDetails(String customerId) {
        CustomerDTO customer = webClient.get()
                .uri("/{customerId}", customerId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (error) -> Mono.error(new ResourceNotFoundException("Customer with ID: " + customerId + " Not Found!")))
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new Exception("SomeThing went wrong!")))
                .bodyToMono(CustomerDTO.class)
                .block();
        log.info("Customer info: {}", customer);
        return customer;
    }
    public void updateCustomerWallet(CustomerDTO customer){
        log.info("Update Customer Wallet: {}", Log.requestResponseData(customer));
        String customerId = customer.getId();
        CustomerDTO updatedCustomer = webClient.put()
                .uri("/{customerId}", customerId)
                .bodyValue(customer)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new ResourceNotFoundException("No Customer Found!!")))
                .onStatus(HttpStatusCode::isError,clientResponse -> Mono.error(new Exception("Something went wrong!")))
                .bodyToMono(CustomerDTO.class)
                .block();
        log.info("Updated Customer Detail: {}", Log.requestResponseData(updatedCustomer));
    }
}
