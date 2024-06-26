package com.example.FF_FastFood.service;

import com.example.FF_FastFood.entity.Account;
import com.example.FF_FastFood.entity.Customer;

public interface AccountService {

    Account authenticateUser(String username, String password);

    Customer findCustomerByAccountId(Long accountId);

    boolean existsByUsername(String username);

    void saveAccount(Account account);

    void saveCustomer(Customer customer);

    Customer findCustomerByEmail(String email);

    boolean validateResetCode(Customer customer, String resetCode);

    String hashPassword(String password);
}
