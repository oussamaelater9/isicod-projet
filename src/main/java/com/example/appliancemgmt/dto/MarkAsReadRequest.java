package com.example.appliancemgmt.dto;

import jakarta.validation.constraints.NotNull;

public class MarkAsReadRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MarkAsReadRequest{userId=" + userId + "}";
    }
}