package com.amount.customers.dao;

import com.amount.customers.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
    Optional<Customer> findByEmailIgnoreCase(String email);
}
