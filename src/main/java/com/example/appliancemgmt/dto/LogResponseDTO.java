package com.example.appliancemgmt.dto;

import java.time.LocalDateTime;

public class LogResponseDTO {
    private Long id;
    private LocalDateTime timestamp;
    private String action;
    private String entity;
    private String details;
    private UserDTO user;
    private LocalDateTime createdAt;

    public LogResponseDTO(Long id, LocalDateTime timestamp, String action, String entity, String details, UserDTO user, LocalDateTime createdAt) {
        this.id = id;
        this.timestamp = timestamp;
        this.action = action;
        this.entity = entity;
        this.details = details;
        this.user = user;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntity() { return entity; }
    public void setEntity(String entity) { this.entity = entity; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class UserDTO {
        private String username;

        public UserDTO(String username) {
            this.username = username;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}