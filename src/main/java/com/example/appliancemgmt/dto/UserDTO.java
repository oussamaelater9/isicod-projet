package com.example.appliancemgmt.dto;

import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String createdAt;
    private List<ClientResponseDTO> clients;

    public UserDTO(Long id, String username, String name, String email, String phone, String address, String role, String createdAt, List<ClientResponseDTO> clients) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.createdAt = createdAt;
        this.clients = clients;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
    public String getCreatedAt() { return createdAt; }
    public List<ClientResponseDTO> getClients() { return clients; }
}