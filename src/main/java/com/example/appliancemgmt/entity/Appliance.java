package com.example.appliancemgmt.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appliances")
public class Appliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
        private LocalDate testStartDate;

    @Column
    private LocalDate testEndDate;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public enum Status { ACTIVE, INACTIVE, REPAIRED }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDate getTestStartDate() { return testStartDate; }
    public void setTestStartDate(LocalDate testStartDate) { this.testStartDate = testStartDate; }
    public LocalDate getTestEndDate() { return testEndDate; }
    public void setTestEndDate(LocalDate testEndDate) { this.testEndDate = testEndDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}