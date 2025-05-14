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
import org.springframework.security.core.context.SecurityContextHolder;
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
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
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
            // Only SUPERADMIN can create SUPERADMIN users
            String currentUserRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .findFirst().map(auth -> auth.getAuthority().replace("ROLE_", "")).orElse("");
            if (user.getRole() == Role.SUPERADMIN && !currentUserRole.equals("SUPERADMIN")) {
                throw new IllegalArgumentException("Only SuperAdmin can create SuperAdmin users");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    public User updateUser(Long id, UserDTO userDTO) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (!existingUserOpt.isPresent()) {
            logger.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User with id " + id + " not found");
        }

        User existingUser = existingUserOpt.get();
        String currentUserRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst().map(auth -> auth.getAuthority().replace("ROLE_", "")).orElse("");

        // Only SUPERADMIN can change roles
//        if (userDTO.getRole() != null && !currentUserRole.equals("SUPERADMIN")) {
//            throw new IllegalArgumentException("Only SuperAdmin can change user roles");
//        }

        if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
            Optional<User> usernameCheck = userRepository.findByUsername(userDTO.getUsername());
            if (usernameCheck.isPresent() && !usernameCheck.get().getId().equals(id)) {
                logger.warn("Username {} already exists", userDTO.getUsername());
                throw new IllegalArgumentException("Username already exists");
            }
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getName() != null) {
            existingUser.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhone() != null) {
            existingUser.setPhone(userDTO.getPhone());
        }
        if (userDTO.getAddress() != null) {
            existingUser.setAddress(userDTO.getAddress());
        }
        if (userDTO.getRole() != null && currentUserRole.equals("SUPERADMIN")) {
            existingUser.setRole(Role.valueOf(userDTO.getRole()));
        }
        try {
            return userRepository.save(existingUser);
        } catch (Exception e) {
            logger.error("Error updating user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update user: " + e.getMessage());
        }
    }

    public void deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            logger.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User with id " + id + " not found");
        }

        User userToDelete = userOpt.get();
        String currentUserRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst().map(auth -> auth.getAuthority().replace("ROLE_", "")).orElse("");
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Super Admin can delete any user, including other Super Admins
        if (currentUserRole.equals("SUPERADMIN")) {
            try {
                userRepository.deleteById(id);
                return; // Exit after deletion
            } catch (Exception e) {
                logger.error("Error deleting user: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to delete user: " + e.getMessage());
            }
        }

        // Prevent ADMIN from deleting other ADMINs or SUPERADMINs
        if (currentUserRole.equals("ADMIN") &&
                (userToDelete.getRole() == Role.ADMIN || userToDelete.getRole() == Role.SUPERADMIN)) {
            throw new IllegalArgumentException("Admins cannot delete other Admins or Super Admins");
        }

        // Prevent CONSULTANT from deleting ADMINs or SUPERADMINs
        if (currentUserRole.equals("CONSULTANT") &&
                (userToDelete.getRole() == Role.ADMIN || userToDelete.getRole() == Role.SUPERADMIN)) {
            throw new IllegalArgumentException("Consultants cannot delete Admins or Super Admins");
        }

        // Prevent self-deletion for non-Super Admins
        Optional<User> currentUserOpt = userRepository.findByUsername(currentUsername);
        if (currentUserOpt.isPresent() && currentUserOpt.get().getId().equals(id)) {
            throw new IllegalArgumentException("Users cannot delete themselves");
        }

        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }
}