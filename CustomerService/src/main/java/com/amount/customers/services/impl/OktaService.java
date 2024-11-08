package com.amount.customers.services.impl;

import com.amount.customers.dto.CustomerDTO;
import com.amount.customers.dto.OktaRequestUser;
import com.amount.customers.models.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class OktaService {
    @Autowired
    private WebClient webClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${okta.app.id}")
    private String oktaApplicationID;
    @Value("${okta.app.role.customer.id}")
    private String customerRoleId;
    public void registerUserWithOkta(CustomerDTO customerDTO){

        OktaRequestUser requestUser = OktaRequestUser.builder()
                .profile(OktaRequestUser.Profile.builder().email(customerDTO.getEmail()).firstName(customerDTO.getFirstName()).login(customerDTO.getEmail()).lastName(customerDTO.getLastName()).build())
                .groupIds(List.of(customerRoleId))
                .credentials(OktaRequestUser.Credentials.builder().password(OktaRequestUser.Password.builder().value(customerDTO.getPassword()).build()).build())
                .build();

        String body = null;
        try {
            body = objectMapper.writer().writeValueAsString(requestUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("body: {}", body);
        String result = webClient.post().uri("/api/v1/users?activate=true").bodyValue(body).retrieve().onStatus(HttpStatusCode::is4xxClientError, response-> response.bodyToMono(String.class).flatMap(error-> Mono.error(new RuntimeException(error)))).bodyToMono(String.class).block();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String id = rootNode.path("id").asText();
        customerDTO.setId(id);
        log.info("Customer Created In Okta with customerId: {}", id);
        String applicationbody = String.format("{\"id\":\"%s\"}", id);
        try{
            webClient.post().uri("/api/v1/apps/"+ oktaApplicationID +"/users").bodyValue(applicationbody).retrieve().bodyToMono(Void.class).block();
        }catch(Exception ex){
            log.error("User could Not be Registered With Okta!");
        }
    }
    public void updateRegisteredCustomer(Customer existingCustomer, CustomerDTO updatedCustomer){
        String body = String.format("{"
                + "\"oldPassword\": { \"value\": \"%s\"},"
                + "\"newPassword\": { \"value\": \"%s\" },"
                + "\"revokeSessions\" : true"
                + "}",existingCustomer.getPassword(), updatedCustomer.getPassword());
        log.info("body: {}", body);
        String responseData = webClient.post().uri("https://dev-82346525.okta.com/api/v1/users/" + existingCustomer.getId() + "/credentials/change_password").bodyValue(body).retrieve().onStatus(HttpStatusCode::is4xxClientError, response-> response.bodyToMono(String.class).flatMap(error-> Mono.error(new RuntimeException(error)))).bodyToMono(String.class).block();
        log.info("Okta Customer Updated: {}" ,responseData);
    }
}
