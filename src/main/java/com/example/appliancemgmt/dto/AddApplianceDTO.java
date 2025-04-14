package com.example.appliancemgmt.dto;

import java.time.LocalDate;

public class AddApplianceDTO {
    private String name;
    private String status;
    private Long clientId;
    private LocalDate testStartDate;
    private LocalDate testEndDate;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public LocalDate getTestStartDate() { return testStartDate; }
    public void setTestStartDate(LocalDate testStartDate) { this.testStartDate = testStartDate; }
    public LocalDate getTestEndDate() { return testEndDate; }
    public void setTestEndDate(LocalDate testEndDate) { this.testEndDate = testEndDate; }
}