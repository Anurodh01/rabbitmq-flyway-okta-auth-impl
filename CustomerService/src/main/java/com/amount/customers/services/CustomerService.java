package com.amount.customers.services;

import com.amount.customers.dto.CustomerDTO;
import com.amount.customers.models.Customer;
import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(String id);
    CustomerDTO updateCustomer(CustomerDTO customer, String id);
    void deleteCustomer(String id);
}
