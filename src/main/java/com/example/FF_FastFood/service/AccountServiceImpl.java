package com.example.FF_FastFood.service;

import com.example.FF_FastFood.entity.Account;
import com.example.FF_FastFood.entity.Customer;
import com.example.FF_FastFood.repository.AccountRepository;
import com.example.FF_FastFood.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Account authenticateUser(String username, String password) {
        Account user = accountRepository.findByUsername(username);
        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }


    @Override
    public Customer findCustomerByAccountId(Long accountId) {
        return customerRepository.findByAccountId(accountId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public boolean validateResetCode(Customer customer, String resetCode) {
        if (customer.getAccount().getPasswordResetCode().equals(resetCode)
                && customer.getAccount().getResetCodeExpiration().after(new Date())) {
            return true;
        }
        return false;
    }

    @Override
    public String hashPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

}
