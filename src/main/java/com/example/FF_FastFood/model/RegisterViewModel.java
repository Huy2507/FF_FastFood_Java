package com.example.FF_FastFood.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterViewModel {
    private String username;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;

}
