package com.amount.customers;

import com.amount.customers.dao.CustomerRepository;
import com.amount.customers.dto.CustomerDTO;
import com.amount.customers.exception.CustomException;
import com.amount.customers.exception.ErrorCode;
import com.amount.customers.exception.ResourceNotFoundException;
import com.amount.customers.models.Customer;
import com.amount.customers.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {
//    @Mock
//    private CustomerRepository customerRepository;
//    @InjectMocks
//    private CustomerServiceImpl customerService;
//    private CustomerDTO customerDTO;
//    private Customer customer;
//    @BeforeEach
//    public void setUp(){
//        customerDTO = new CustomerDTO();
//        customerDTO.setFirstName("customer1");
//        customerDTO.setActive(true);
//        customerDTO.setAge(20);
//        customerDTO.setWalletBalance(1000.00);
//
//        customer = new Customer();
//        customer.setId(1);
//        customer.setFirstName("customer1");
//        customer.setActive(true);
//        customer.setAge(20);
//        customer.setWalletBalance(1000);
//    }
//
////    @Test
////    @DisplayName("JUnit test for Create Customer method")
////    public void CreateCustomerSuccessTest(){
////        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
////        when(customerRepository.findByNameIgnoreCase("customer1")).thenReturn(Optional.empty());
////        CustomerDTO savedCustomer = customerService.createCustomer(customerDTO);
////        assertEquals(savedCustomer.getName(), customer.getName());
////        verify(customerRepository, times(1)).save(customer);
////    }
//
//    @Test
//    @DisplayName("JUnit test for Create Customer method On Customer Name already Exist Exception throw")
//    public void CreateCustomerNameAlreadyExistExceptionTest(){
//        when(customerRepository.findByNameIgnoreCase("customer1")).thenThrow(new CustomException(ErrorCode.CUSTOMER_ALREADY_EXIST));
//        assertThrows(CustomException.class, ()-> customerService.createCustomer(customerDTO));
//        verify(customerRepository, never()).save(any(Customer.class));
//    }
//
//    @Test
//    @DisplayName("JUnit test for getCustomerById method")
//    public void getCustomerByIdSuccessTest(){
//        when(customerRepository.findById(1)).thenReturn(Optional.ofNullable(customer));
//        CustomerDTO returnedCustomer = customerService.getCustomerById(1);
//        assertEquals(customer.getFirstName(), returnedCustomer.getFirstName());
//        assertEquals(customer.getAge(), returnedCustomer.getAge());
//        assertEquals(customer.getWalletBalance(), returnedCustomer.getWalletBalance());
//        verify(customerRepository, times(1)).findById(1);
//    }
//
//    @Test
//    @DisplayName("JUnit Test for getCustomerById method ResourceNotFoundException thrown")
//    public void getCustomerByIdResourceNotFoundExceptionThrown(){
//        when(customerRepository.findById(10)).thenThrow(new ResourceNotFoundException("Customer with id: "+ 1 +" does not exist!!"));
//        assertThrows(ResourceNotFoundException.class, ()->{
//            customerService.getCustomerById(10);
//        });
//    }
//    @Test
//    @DisplayName("JUnit Test for getAllCustomers method")
//    public void getAllCustomersSuccess(){
//        Customer customer1= new Customer(2,"customer2",33,200052,false);
//        List<Customer> customers = Arrays.asList(customer, customer1);
//
//        when(customerRepository.findAll()).thenReturn(customers);
//        List<CustomerDTO> allCustomers = customerService.getAllCustomers();
//        assertEquals(2, allCustomers.size());
//        verify(customerRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("JUnit test for Update Customer method")
//    public void updateCustomerSuccess(){
//
//        when(customerRepository.findById(1)).thenReturn(Optional.ofNullable(customer));
//        customer.setFirstName("UpdatedCustomer");
//        customer.setAge(30);
//        customer.setWalletBalance(2000);
//        customerDTO.setFirstName("UpdatedCustomer");
//        customerDTO.setWalletBalance(2000.00);
//        customerDTO.setAge(30);
//        when(customerRepository.save(customer)).thenReturn(customer);
//        CustomerDTO result = customerService.updateCustomer(customerDTO, 1);
//        assertEquals(result.getFirstName(),"UpdatedCustomer");
//        assertEquals(result.getWalletBalance(), 2000);
//        assertNotEquals(result.getAge(), 20);
//        verify(customerRepository, times(1)).findById(1);
//        verify(customerRepository, times(1)).save(customer);
//    }
//
//    @Test
//    @DisplayName("JUnit test for the DeleteMethod of customer")
//    public void deleteCustomerSuccess(){
//        when(customerRepository.findById(1)).thenReturn(Optional.ofNullable(customer));
//        customer.setActive(false);
//        when(customerRepository.save(customer)).thenReturn(customer);
//        customerService.deleteCustomer(1);
//        assertFalse(customer.isActive());
//        verify(customerRepository, times(1)).findById(1);
//        verify(customerRepository, times(1)).save(customer);
//    }


}
