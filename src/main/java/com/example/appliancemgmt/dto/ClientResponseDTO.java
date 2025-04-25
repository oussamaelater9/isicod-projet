package com.example.appliancemgmt.dto;

public class ClientResponseDTO {
    private Long id;
    private String name;

    public ClientResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}