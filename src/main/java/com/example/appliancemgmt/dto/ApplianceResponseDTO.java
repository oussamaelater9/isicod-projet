package com.example.appliancemgmt.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplianceResponseDTO {
    private Long id;
    private String name;
    private String status;
    private LocalDate testStartDate;
    private LocalDate testEndDate;
    private LocalDateTime createdAt;
    private ClientSummaryDTO client;

    public static class ClientSummaryDTO {
        private Long id;
        private String companyName;
        private String name;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getTestStartDate() { return testStartDate; }
    public void setTestStartDate(LocalDate testStartDate) { this.testStartDate = testStartDate; }
    public LocalDate getTestEndDate() { return testEndDate; }
    public void setTestEndDate(LocalDate testEndDate) { this.testEndDate = testEndDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ClientSummaryDTO getClient() { return client; }
    public void setClient(ClientSummaryDTO client) { this.client = client; }
}