package com.example.appliancemgmt.dto;

import com.example.appliancemgmt.entity.SessionStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AddSessionDTO {

    @NotNull(message = "Session date is required")
    private LocalDateTime sessionDate;

    @NotNull(message = "Status is required")
    private SessionStatus status;

    private String notes;

    // Getters and Setters
    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}