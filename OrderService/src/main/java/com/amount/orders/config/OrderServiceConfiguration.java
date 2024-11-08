package com.amount.orders.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Configuration
public class OrderServiceConfiguration {
    @Value("${CUSTOMER_SERVICE_URL}")
    private String CUSTOMER_SERVICE_URL;
//    @Bean
//    public WebClient webClient(){
//        WebClient client = WebClient.builder()
//                .baseUrl(CUSTOMER_SERVICE_URL)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
////                .defaultHeader("Authorization", "Basic "+ Base64.getEncoder().encodeToString(("customerService" + ":" + "password").getBytes()))
//                .build();
//
//        return client;
//    }

}
