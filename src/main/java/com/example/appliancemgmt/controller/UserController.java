package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.ApiResponse;
import com.example.appliancemgmt.dto.ClientResponseDTO;
import com.example.appliancemgmt.dto.UserDTO;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

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
    public ApiResponse<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "User created successfully", createdUser);
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ApiResponse<>(HttpStatus.OK.value(), "User updated successfully", updatedUser);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully", null);
    }
}