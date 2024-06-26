package com.example.FF_FastFood.repository;

import com.example.FF_FastFood.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByAccountId(Long accountId);

    Customer findByEmail(String email);
}
