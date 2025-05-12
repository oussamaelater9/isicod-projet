package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.ApiResponse;
import com.example.appliancemgmt.dto.ClientResponseDTO;
import com.example.appliancemgmt.dto.SignUpRequest;
import com.example.appliancemgmt.dto.UserDTO;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.service.UserService;
import com.example.appliancemgmt.service.LogService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @GetMapping
    public ApiResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getAddress(),
                        user.getRole().name(),
                        user.getCreatedAt().toString(),
                        user.getClients().stream()
                                .map(client -> new ClientResponseDTO(
                                        client.getId(),
                                        client.getName()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return new ApiResponse<>(HttpStatus.OK.value(), "Users retrieved successfully", users);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole().name(),
                user.getCreatedAt().toString(),
                user.getClients().stream()
                        .map(client -> new ClientResponseDTO(
                                client.getId(),
                                client.getName()
                        ))
                        .collect(Collectors.toList())
        );
        return new ApiResponse<>(HttpStatus.OK.value(), "User retrieved successfully", userDTO);
    }

    @GetMapping("/username/{username}")
    public ApiResponse<UserDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole().name(),
                user.getCreatedAt().toString(),
                user.getClients().stream()
                        .map(client -> new ClientResponseDTO(
                                client.getId(),
                                client.getName()
                        ))
                        .collect(Collectors.toList())
        );
        return new ApiResponse<>(HttpStatus.OK.value(), "User retrieved successfully", userDTO);
    }

    @GetMapping("/role/{role}")
    public ApiResponse<List<UserDTO>> getUsersByRole(@PathVariable User.Role role) {
        List<UserDTO> users = userService.getUsersByRole(role)
                .stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getAddress(),
                        user.getRole().name(),
                        user.getCreatedAt().toString(),
                        user.getClients().stream()
                                .map(client -> new ClientResponseDTO(
                                        client.getId(),
                                        client.getName()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return new ApiResponse<>(HttpStatus.OK.value(), "Users retrieved successfully", users);
    }

    @PostMapping
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody SignUpRequest request) {
        try {
            User createdUser = userService.signUp(request);
            logService.logAction("CREATE", "User", "Created user with username: " + createdUser.getUsername());
            UserDTO userDTO = new UserDTO(
                    createdUser.getId(),
                    createdUser.getUsername(),
                    createdUser.getName(),
                    createdUser.getEmail(),
                    createdUser.getPhone(),
                    createdUser.getAddress(),
                    createdUser.getRole().name(),
                    createdUser.getCreatedAt().toString(),
                    createdUser.getClients().stream()
                            .map(client -> new ClientResponseDTO(
                                    client.getId(),
                                    client.getName()
                            ))
                            .collect(Collectors.toList())
            );
            return new ApiResponse<>(HttpStatus.CREATED.value(), "User created successfully", userDTO);
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Unexpected error during user creation", e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage(), null);
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(id, userDTO);
        UserDTO updatedUserDTO = new UserDTO(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getAddress(),
                updatedUser.getRole().name(),
                updatedUser.getCreatedAt().toString(),
                updatedUser.getClients().stream()
                        .map(client -> new ClientResponseDTO(client.getId(), client.getName()))
                        .collect(Collectors.toList())
        );
        logService.logAction("UPDATE", "User", "Updated user with ID: " + id);
        return new ApiResponse<>(HttpStatus.OK.value(), "User updated successfully", updatedUserDTO);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        logService.logAction("DELETE", "User", "Deleted user with ID: " + id);
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully", null);
    }
}