package com.example.appliancemgmt.dto;

import java.time.LocalDateTime;

public class AddEvaluationDTO {
    private LocalDateTime meetingDate;
    private String outcome;
    private String notes;
    private Long applianceId;

    // Getters and Setters
    public LocalDateTime getMeetingDate() { return meetingDate; }
    public void setMeetingDate(LocalDateTime meetingDate) { this.meetingDate = meetingDate; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getApplianceId() { return applianceId; }
    public void setApplianceId(Long applianceId) { this.applianceId = applianceId; }
}