//package com.example.FF_FastFood.service;
//
//import com.example.FF_FastFood.entity.Account;
//import com.example.FF_FastFood.entity.Customer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    public Account authenticateUser(String username, String password) {
//        Account user = accountRepository.findByUsername(username);
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            return user;
//        }
//        return null;
//    }
//
//    public Customer findCustomerByAccountId(int accountId) {
//        return customerRepository.findByAccountId(accountId);
//    }
//
//    public boolean existsByUsername(String username) {
//        return accountRepository.existsByUsername(username);
//    }
//
//    public void saveAccount(Account account) {
//        accountRepository.save(account);
//    }
//
//    public void saveCustomer(Customer customer) {
//        customerRepository.save(customer);
//    }
//
//    public Customer findCustomerByEmail(String email) {
//        return customerRepository.findByEmail(email);
//    }
//
//    public boolean validateResetCode(Customer customer, String resetCode) {
//        if (customer.getAccount().getPasswordResetCode().equals(resetCode)
//                && customer.getAccount().getResetCodeExpiration().after(new Date())) {
//            return true;
//        }
//        return false;
//    }
//
//    public String hashPassword(String password) {
//        return passwordEncoder.encode(password);
//    }
//}
