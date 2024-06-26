package com.example.FF_FastFood.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyResetCodeViewModel {
    private String email;
    private String resetCode;

}
