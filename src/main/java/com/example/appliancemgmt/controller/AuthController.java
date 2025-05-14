package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.ApiResponse;
import com.example.appliancemgmt.dto.SignUpRequest;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.service.UserService;
import com.example.appliancemgmt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ApiResponse<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        User user = userService.signUp(signUpRequest);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "User created successfully", user.getUsername());
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.getUserByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty() ||
                !passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Identifiants invalides", null);
        }

        User user = userOptional.get();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return new ApiResponse<>(HttpStatus.OK.value(), "Connexion réussie", token);
    }


    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ApiResponse<Void> handleOptions() {
        return new ApiResponse<>(HttpStatus.OK.value(), "OPTIONS request handled", null);
    }
}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}