package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.ApiResponse;
import com.example.appliancemgmt.entity.Role;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ApiResponse<>(HttpStatus.OK.value(), "Users retrieved successfully", users);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return new ApiResponse<>(HttpStatus.OK.value(), "User retrieved successfully", user);
    }

    @GetMapping("/username/{username}")
    public ApiResponse<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return new ApiResponse<>(HttpStatus.OK.value(), "User retrieved successfully", user);
    }

    @GetMapping("/role/{role}")
    public ApiResponse<List<User>> getUsersByRole(@PathVariable User.Role role) {
        List<User> users = userService.getUsersByRole(role);
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