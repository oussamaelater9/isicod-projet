package com.example.appliancemgmt.service;

import com.example.appliancemgmt.dto.AddApplianceDTO;
import com.example.appliancemgmt.dto.ApplianceResponseDTO;
import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.entity.Client;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.repository.ApplianceRepository;
import com.example.appliancemgmt.repository.ClientRepository;
import com.example.appliancemgmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplianceService {

    @Autowired
    private ApplianceRepository applianceRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    // Utility method to capitalize the first letter of a string
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public Appliance addAppliance(AddApplianceDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // Get current user from JWT
        String username = "anonymous";
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                username = SecurityContextHolder.getContext().getAuthentication().getName();
            }
        } catch (Exception e) {
            // Log error if needed
        }

        String finalUsername = username;
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + finalUsername));

        Appliance appliance = new Appliance();
        appliance.setName(dto.getName());
        appliance.setStatus(dto.getStatus() != null ? Appliance.Status.valueOf(dto.getStatus()) : null);
        appliance.setTestStartDate(dto.getTestStartDate());
        appliance.setTestEndDate(dto.getTestEndDate());
        appliance.setClient(client);

        // Create notification with appliance name, capitalized username, and client name
        String notificationMessage = String.format(
                "New appliance %s added by %s for client %s",
                appliance.getName(),
                capitalizeFirstLetter(user.getUsername()),
                client.getName()
        );
        notificationService.createNotification(notificationMessage, user.getId(), client.getId());

        return applianceRepository.save(appliance);
    }

    public List<ApplianceResponseDTO> getAllAppliances() {
        return applianceRepository.findAll().stream()
                .map(appliance -> {
                    ApplianceResponseDTO dto = new ApplianceResponseDTO();
                    dto.setId(appliance.getId());
                    dto.setName(appliance.getName());
                    dto.setStatus(appliance.getStatus() != null ? appliance.getStatus().name() : null);
                    dto.setTestStartDate(appliance.getTestStartDate());
                    dto.setTestEndDate(appliance.getTestEndDate());
                    dto.setCreatedAt(appliance.getCreatedAt());
                    ApplianceResponseDTO.ClientSummaryDTO clientDto = new ApplianceResponseDTO.ClientSummaryDTO();
                    clientDto.setId(appliance.getClient().getId());
                    clientDto.setCompanyName(appliance.getClient().getCompanyName());
                    clientDto.setName(appliance.getClient().getName());
                    dto.setClient(clientDto);
                    return dto;
                })
                .toList();
    }

    public Appliance updateAppliance(Long id, AddApplianceDTO dto) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appliance not found"));
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        appliance.setName(dto.getName());
        appliance.setStatus(dto.getStatus() != null ? Appliance.Status.valueOf(dto.getStatus()) : null);
        appliance.setTestStartDate(dto.getTestStartDate());
        appliance.setTestEndDate(dto.getTestEndDate());
        appliance.setClient(client);
        return applianceRepository.save(appliance);
    }

    public void deleteAppliance(Long id) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appliance not found"));
        applianceRepository.delete(appliance);
    }
}