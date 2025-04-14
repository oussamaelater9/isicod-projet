package com.example.appliancemgmt.service;

import com.example.appliancemgmt.dto.AddClientDTO;
import com.example.appliancemgmt.entity.Client;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.repository.ClientRepository;
import com.example.appliancemgmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name);
    }

    public Client createClient(AddClientDTO client,Long id) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (client.getName() == null || client.getEmail() == null) {
            throw new IllegalArgumentException("Name and email are required");
        }


        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
        Client newClient = new Client();
        if (user != null) {

            newClient.setAddress(client.getAdress());
            newClient.setPhone(client.getPhone());
            newClient.setCompanyName(client.getCompanyName());
            newClient.setEmail(client.getEmail());
            newClient.setName(client.getName());
            newClient.setUser(user);

        }

        return clientRepository.save(newClient);

    }

    public Client updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id)
                .map(existing -> {
                    if (!existing.getEmail().equals(updatedClient.getEmail()) &&
                            clientRepository.existsByEmail(updatedClient.getEmail())) {
                        throw new IllegalArgumentException("Email already taken");
                    }
                    existing.setName(updatedClient.getName());
                    existing.setEmail(updatedClient.getEmail());
                    existing.setPhone(updatedClient.getPhone());
                    return clientRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));
    }

    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }
}