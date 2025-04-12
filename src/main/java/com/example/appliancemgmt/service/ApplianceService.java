package com.example.appliancemgmt.service;

import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.entity.ApplianceStatus;
import com.example.appliancemgmt.entity.Client;
import com.example.appliancemgmt.repository.ApplianceRepository;
import com.example.appliancemgmt.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApplianceService {
    @Autowired
    private ApplianceRepository applianceRepository;

    @Autowired
    private ClientRepository clientRepository;

    public List<Appliance> getAllAppliances() {
        return applianceRepository.findAll();
    }

    public Optional<Appliance> getApplianceById(Long id) {
        return applianceRepository.findById(id);
    }

    public List<Appliance> getAppliancesByClientId(Long clientId) {
        return applianceRepository.findByClientId(clientId);
    }

    public List<Appliance> getAppliancesByStatus(ApplianceStatus status) {
        return applianceRepository.findByStatus(status);
    }

    public List<Appliance> searchAppliancesByName(String name) {
        return applianceRepository.findByNameContainingIgnoreCase(name);
    }

    public Appliance createAppliance(Appliance appliance, Long clientId) {
        if (appliance.getName() == null || appliance.getStatus() == null) {
            throw new IllegalArgumentException("Name and status are required");
        }
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isPresent()) {
            appliance.setClient(client.get());
        } else {
            throw new IllegalArgumentException("Client not found with id: " + clientId);
        }
        return applianceRepository.save(appliance);
    }

    public Appliance updateAppliance(Long id, Appliance updatedAppliance, Long clientId) {
        return applianceRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedAppliance.getName());
                    existing.setTestStartDate(updatedAppliance.getTestStartDate());
                    existing.setTestEndDate(updatedAppliance.getTestEndDate());
                    existing.setStatus(updatedAppliance.getStatus());
                    if (clientId != null) {
                        Client client = clientRepository.findById(clientId)
                                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));
                        existing.setClient(client);
                    }
                    return applianceRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Appliance not found with id: " + id));
    }

    public void deleteAppliance(Long id) {
        if (!applianceRepository.existsById(id)) {
            throw new IllegalArgumentException("Appliance not found with id: " + id);
        }
        applianceRepository.deleteById(id);
    }
}