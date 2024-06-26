package com.example.FF_FastFood.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long Id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "password_reset_code")
    private String passwordResetCode;


    @Getter
    @Column(name = "reset_code_expiration")
    private Date resetCodeExpiration;


//     getters and setters
}
