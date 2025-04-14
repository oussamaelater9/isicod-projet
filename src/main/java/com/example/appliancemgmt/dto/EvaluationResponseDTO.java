package com.example.appliancemgmt.dto;

import java.time.LocalDateTime;

public class EvaluationResponseDTO {
    private Long id;
    private LocalDateTime meetingDate;
    private String outcome;
    private String notes;
    private LocalDateTime createdAt;
    private ApplianceSummaryDTO appliance;

    public static class ApplianceSummaryDTO {
        private Long id;
        private String name;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getMeetingDate() { return meetingDate; }
    public void setMeetingDate(LocalDateTime meetingDate) { this.meetingDate = meetingDate; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ApplianceSummaryDTO getAppliance() { return appliance; }
    public void setAppliance(ApplianceSummaryDTO appliance) { this.appliance = appliance; }
}