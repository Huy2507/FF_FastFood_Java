package com.example.FF_FastFood.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;

    private JavaMailSender javaMailSender;

    public EmailService(@Value("${spring.mail.host}") String host,
                        @Value("${spring.mail.port}") int port,
                        @Value("${spring.mail.username}") String username,
                        @Value("${spring.mail.password}") String password,
                        @Value("${spring.mail.properties.mail.smtp.auth}") String smtpAuth,
                        @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String starttlsEnable) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.smtpAuth = smtpAuth;
        this.starttlsEnable = starttlsEnable;
        this.javaMailSender = getJavaMailSender();
    }

    public void sendResetCodeEmail(String toEmail, String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(toEmail);
        message.setSubject("Reset Password");
        message.setText("Your reset code is: " + resetCode);
        javaMailSender.send(message);
    }

    private JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);

        return mailSender;
    }
}
