package com.example.appliancemgmt.service;

import com.example.appliancemgmt.entity.PasswordResetToken;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.repository.PasswordResetTokenRepository;
import com.example.appliancemgmt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final Logger log =
            LoggerFactory.getLogger(PasswordResetService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void forgotPassword(String email) {

        log.info("Password reset requested for email: {}", email);

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            log.warn("Password reset requested for unknown email: {}", email);
            return;
        }

        User user = optionalUser.get();

        // Delete any existing token for this user
        tokenRepository.findByUserId(user.getId())
                .ifPresent(existingToken -> {
                    tokenRepository.delete(existingToken);
                    tokenRepository.flush();
                });

        // Generate new token
        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken(
                token,
                LocalDateTime.now().plusMinutes(30),
                user
        );

        tokenRepository.save(passwordResetToken);

        log.info("Password reset token generated for user {}", user.getUsername());

        emailService.sendPasswordResetEmail(user.getEmail(), token);

        log.info("Password reset email sent successfully to {}", user.getEmail());
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        log.info("Password reset attempt using token.");

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            log.warn("Expired password reset token used.");

            tokenRepository.delete(resetToken);

            throw new IllegalArgumentException("Token expired.");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        
        tokenRepository.delete(resetToken);
        tokenRepository.flush();

        log.info("Password successfully changed for user {}", user.getUsername());
    }
}