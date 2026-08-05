package com.example.appliancemgmt.service;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from}")
    private String from;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(String to, String token) {

        try {

            String resetLink =
                    frontendUrl + "/reset-password?token=" + token;

            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(from)
                    .to(to)
                    .subject("Reset your Appliance Management password")
                    .html("""
                            <h2>Password Reset</h2>

                            <p>We received a request to reset your password.</p>

                            <p>
                                <a href="%s">
                                    Reset Password
                                </a>
                            </p>

                            <p>This link expires in 30 minutes.</p>

                            <p>If you didn't request this, simply ignore this email.</p>
                            """.formatted(resetLink))
                    .build();

            resend.emails().send(params);

            log.info("Password reset email sent to {}", to);

        } catch (Exception e) {

            log.error("Failed to send email", e);

            throw new RuntimeException("Unable to send email", e);
        }
    }
}