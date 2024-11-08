package com.amount.orders;

import com.amount.orders.dto.CustomerDTO;
import com.amount.orders.util.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class OrderServiceApplication{

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

//	INORDER TO CHECK THE CLIENT REQUEST THROUGH THE WEBCLIENT

//	@Autowired
//	private WebClient webClient;
//
//	@Override
//	public void run(String... args) throws Exception {
//		//call the webclient
//		int customerId = 1;
//		CustomerDTO customerDTO = webClient.get()
//				.uri("/{customerId}", customerId)
//				.retrieve()
//				.bodyToMono(CustomerDTO.class)
//				.block();
//
//		log.info("DATA received: {}", Log.requestResponseData(customerDTO));
//
//	}

//	INORDER TO CHECK THE CLIENT REQUEST THROUGH THE REST TEMPLATE

//	@Autowired
//	private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

//	@Override
//	public void run(String... args) throws Exception {
//		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("okta").principal("demo service").build();
//		OAuth2AuthorizedClient authorizedClient = authorizedClientServiceAndManager.authorize(authorizeRequest);
//
//		OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();
//		log.info("Issued: " + accessToken.getIssuedAt().toString() + ", Expires:" + accessToken.getExpiresAt().toString());
//		log.info("Scopes: " + accessToken.getScopes().toString());
//		log.info("Token: " + accessToken.getTokenValue());
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Authorization","Bearer "+accessToken.getTokenValue());
//		HttpEntity<CustomerDTO> request = new HttpEntity<>(headers);
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<CustomerDTO> customer = restTemplate.exchange("http://localhost:8080/api/v1/customers/2", HttpMethod.GET, request, CustomerDTO.class);
//		log.info("Status: {}, DATA: {}", customer.getStatusCode(), customer.getBody());
//
//	}
}
