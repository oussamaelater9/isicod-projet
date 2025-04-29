package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.AddClientDTO;
import com.example.appliancemgmt.dto.ApiResponse;
import com.example.appliancemgmt.entity.Client;
import com.example.appliancemgmt.service.ClientService;
import com.example.appliancemgmt.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private LogService logService;

    @GetMapping
    public ApiResponse<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return new ApiResponse<>(HttpStatus.OK.value(), "Clients retrieved successfully", clients);
    }

    @GetMapping("/{id}")
    public ApiResponse<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));
        return new ApiResponse<>(HttpStatus.OK.value(), "Client retrieved successfully", client);
    }

    @GetMapping("/email/{email}")
    public ApiResponse<Client> getClientByEmail(@PathVariable String email) {
        Client client = clientService.getClientByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with email: " + email));
        return new ApiResponse<>(HttpStatus.OK.value(), "Client retrieved successfully", client);
    }

    @GetMapping("/search")
    public ApiResponse<List<Client>> searchClientsByName(@RequestParam String name) {
        List<Client> clients = clientService.searchClientsByName(name);
        return new ApiResponse<>(HttpStatus.OK.value(), "Clients retrieved successfully", clients);
    }

    @PostMapping
    public ApiResponse<Client> createClient(@RequestBody AddClientDTO client, @RequestParam Long userId) {
        Client createdClient = clientService.createClient(client, userId);
        logService.logAction("CREATE", "Client", "Created client with ID: " + createdClient.getId());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Client created successfully", createdClient);
    }

    @PutMapping("/{id}")
    public ApiResponse<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Client updatedClient = clientService.updateClient(id, client);
        logService.logAction("UPDATE", "Client", "Updated client with ID: " + id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Client updated successfully", updatedClient);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        logService.logAction("DELETE", "Client", "Deleted client with ID: " + id);
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Client deleted successfully", null);
    }
}