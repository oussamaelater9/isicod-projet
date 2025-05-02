package com.example.appliancemgmt.service;

import com.example.appliancemgmt.dto.SignUpRequest;
import com.example.appliancemgmt.dto.UserDTO;
import com.example.appliancemgmt.entity.Client;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.entity.User.Role;
import com.example.appliancemgmt.repository.ClientRepository;
import com.example.appliancemgmt.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;



    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User signUp(SignUpRequest signUpRequest) {
        if (signUpRequest.getUsername() == null || signUpRequest.getPassword() == null) {
            logger.error("Username or password is null");
            throw new IllegalArgumentException("Username and password are required");
        }

        Optional<User> existingUser = userRepository.findByUsername(signUpRequest.getUsername());
        if (existingUser.isPresent()) {
            logger.warn("Username already exists: {}", signUpRequest.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }

        try {
            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword())); // Hash with bcrypt
            user.setAddress(signUpRequest.getAddress());
            user.setEmail(signUpRequest.getEmail());
            user.setPhone(signUpRequest.getPhone());
            user.setName(signUpRequest.getName());
            user.setRole(Role.CONSULTANT);
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save user: " + e.getMessage());
        }
    }

    public User createUser(User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            logger.error("Username or password is null");
            throw new IllegalArgumentException("Username and password are required");
        }

        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            logger.warn("Username already exists: {}", user.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }

        try {
            if (user.getRole() == null) {
                user.setRole(Role.CONSULTANT);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash with bcrypt
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save user: " + e.getMessage());
        }
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, UserDTO user) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (!existingUserOpt.isPresent()) {
            logger.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User with id " + id + " not found");
        }

        User existingUser = existingUserOpt.get();
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            Optional<User> usernameCheck = userRepository.findByUsername(user.getUsername());
            if (usernameCheck.isPresent() && !usernameCheck.get().getId().equals(id)) {
                logger.warn("Username {} already exists", user.getUsername());
                throw new IllegalArgumentException("Username already exists");
            }
            existingUser.setUsername(user.getUsername());
        }
//        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
//            existingUser.setPassword(passwordEncoder.encode(user.getPassword())); // Hash with bcrypt
//        }
//        if (user.getRole() != null) {
//            existingUser.setRole(user.getRole());
//        }
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getAddress() != null) {
            existingUser.setAddress(user.getAddress());
        }
        try {
            return userRepository.save(existingUser);
        } catch (Exception e) {
            logger.error("Error updating user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update user: " + e.getMessage());
        }
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            logger.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }
}