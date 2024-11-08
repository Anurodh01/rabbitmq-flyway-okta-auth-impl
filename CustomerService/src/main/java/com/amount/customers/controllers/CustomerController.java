package com.amount.customers.controllers;

import com.amount.customers.dto.APIResponse;
import com.amount.customers.dto.CustomerDTO;
import com.amount.customers.exception.UnAuthorizedAccessException;
import com.amount.customers.services.CustomerService;
import com.amount.customers.utils.Log;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequestMapping("/api/v1/customers")
@RestController
@CrossOrigin("*")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/auth")
    public String user(@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client, @AuthenticationPrincipal OidcUser user){
        System.out.println(client.getAccessToken().getTokenValue());
        System.out.println(user.getAttributes());
        System.out.println(user.getAuthorities());
        return user.getEmail();
    }
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO){
        log.info("Customer Create Request Data: {}", Log.requestResponseData(customerDTO));
        CustomerDTO createdCustomerDTO = customerService.createCustomer(customerDTO);
        log.info("Customer created with identifier: {}, Response Data: {}", customerDTO.getId(), Log.requestResponseData(createdCustomerDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerDTO);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(@AuthenticationPrincipal OidcUser user){
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_data_access') || hasAuthority('Customer')")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String id, @AuthenticationPrincipal OidcUser loggedInUser){
        CustomerDTO customer = customerService.getCustomerById(id);
        if(Objects.nonNull(loggedInUser) && !loggedInUser.getName().equals(customer.getId())){
            throw new UnAuthorizedAccessException("Unauthorized Access!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@Valid @RequestBody CustomerDTO customerDTO, @PathVariable String id){
        log.info("Customer update Request Data {}, for identifier {}", Log.requestResponseData(customerDTO), id);
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerDTO, id);
        log.info("Customer updated with identifier {}, Response Data: {}", updatedCustomer.getId(), Log.requestResponseData(updatedCustomer));
        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteCustomer(@PathVariable String id){
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new APIResponse("Success", "Customer with id: "+ id +" De-Activated Successfully" ,HttpStatus.OK, LocalDateTime.now()));
    }

}
