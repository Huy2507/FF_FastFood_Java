package com.example.FF_FastFood.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginViewModel {
    private String username;
    private String password;

    private String loginError;
    public void addAttribute(String s) {
        this.loginError = s;
    }

    public void addAttribute(String errorMessage, String s) {

    }
}
