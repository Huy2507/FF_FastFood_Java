package com.example.FF_FastFood.controller;

import com.example.FF_FastFood.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendResetCodeEmail(@RequestParam String toEmail, @RequestParam String resetCode) {
        emailService.sendResetCodeEmail(toEmail, resetCode);
        return "Email sent successfully";
    }
}
