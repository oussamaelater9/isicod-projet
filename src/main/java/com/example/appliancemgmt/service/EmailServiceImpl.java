package com.example.appliancemgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(String to, String token) {

        String resetLink = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Reset your Appliance Management password");

        message.setText("""
Hello,

We received a request to reset your password.

Click the link below:

%s

This link expires in 30 minutes.

If you didn't request a password reset, simply ignore this email.

Regards,
Appliance Management Team
""".formatted(resetLink));

        mailSender.send(message);
    }
}