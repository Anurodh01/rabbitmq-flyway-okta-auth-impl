package com.amount.customers.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CustomerServiceConfiguration {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl("https://dev-82346525.okta.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SSWS " + "00jjTolyomtoSNFHBYzsINLXuRGxk5DeWUoDVBqphz")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}
