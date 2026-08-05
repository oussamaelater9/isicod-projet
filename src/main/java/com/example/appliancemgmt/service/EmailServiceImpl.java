package com.example.appliancemgmt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    private static final Logger log =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${frontend.url:http://localhost:4200}")
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

        try {
            mailSender.send(message);
            log.info("MAIL SENT");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("MAIL ERROR", e);
            throw new RuntimeException(e);
        }
    }
}