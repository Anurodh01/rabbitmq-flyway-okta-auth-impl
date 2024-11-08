package com.amount.customers.services.impl;

import com.amount.customers.dao.CustomerRepository;
import com.amount.customers.dto.CustomerDTO;
import com.amount.customers.dto.OktaRequestUser;
import com.amount.customers.exception.CustomException;
import com.amount.customers.exception.ErrorCode;
import com.amount.customers.exception.ResourceNotFoundException;
import com.amount.customers.models.Customer;
import com.amount.customers.services.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OktaService oktaService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        Optional<Customer> existingCustomer = customerRepository.findByEmailIgnoreCase(customerDTO.getEmail());
        if(existingCustomer.isPresent()){
            log.error("Customer Email: {} already exist!", customerDTO.getFirstName());
            throw new CustomException(ErrorCode.CUSTOMER_ALREADY_EXIST);
        }
        oktaService.registerUserWithOkta(customerDTO);
        Customer customer = dtoToCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerToDTO(savedCustomer);
    }
    @Override
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = customers.stream().map(this::customerToDTO).toList();
        return customerDTOList;
    }
    @Override
    public CustomerDTO getCustomerById(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Customer with id: "+ id +" does not exist!!"));
        return customerToDTO(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO, String id) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Customer with id: "+ id +" does not exist!!"));
        existingCustomer.setFirstName(customerDTO.getFirstName());
        existingCustomer.setLastName(customerDTO.getLastName());
        if(!existingCustomer.getPassword().equals(customerDTO.getPassword())){
            oktaService.updateRegisteredCustomer(existingCustomer, customerDTO);
            existingCustomer.setPassword(customerDTO.getPassword());
        }
        existingCustomer.setAge(customerDTO.getAge());
        existingCustomer.setActive(customerDTO.isActive());
        existingCustomer.setWalletBalance(customerDTO.getWalletBalance());
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerToDTO(updatedCustomer);
    }
    @Override
    public void deleteCustomer(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Customer with id: "+ id +" does not exist!!"));
        customer.setActive(false);
        log.info("Customer with identifier: {} De-Activated successfully!! , Customer: {}", customer.getId(), customer);
        customerRepository.save(customer);
    }

    private Customer dtoToCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setPassword(customerDTO.getPassword());
        customer.setLastName(customerDTO.getLastName());
        customer.setAge(customerDTO.getAge());
        customer.setWalletBalance(customerDTO.getWalletBalance());
        customer.setActive(customerDTO.isActive());
        return customer;
    }

    private CustomerDTO customerToDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setActive(customer.isActive());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setId(customer.getId());
        customerDTO.setPassword(customer.getPassword());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setAge(customer.getAge());
        customerDTO.setWalletBalance(customer.getWalletBalance());
        return customerDTO;
    }
}
