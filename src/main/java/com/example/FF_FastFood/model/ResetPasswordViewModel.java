package com.example.FF_FastFood.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPasswordViewModel {
    private String email;
    private String newPassword;
    private String confirmPassword;

}
