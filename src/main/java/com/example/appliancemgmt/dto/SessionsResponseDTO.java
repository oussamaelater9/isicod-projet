package com.example.appliancemgmt.dto;

public class SessionsResponseDTO {
    private Long id;
    private Long applianceId;
    private String status;
    private String startTime; // ISO 8601 format, e.g., "2025-04-21T10:00:00Z"
    private String notes;

    // Constructors
    public SessionsResponseDTO() {}

    public SessionsResponseDTO(Long id, Long applianceId, String status, String startTime, String notes) {
        this.id = id;
        this.applianceId = applianceId;
        this.status = status;
        this.startTime = startTime;
        this.notes = notes;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplianceId() { return applianceId; }
    public void setApplianceId(Long applianceId) { this.applianceId = applianceId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}