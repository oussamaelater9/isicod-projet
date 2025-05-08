package com.example.appliancemgmt.service;

import com.example.appliancemgmt.dto.AddApplianceDTO;
import com.example.appliancemgmt.dto.ApplianceResponseDTO;
import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.entity.Client;
import com.example.appliancemgmt.repository.ApplianceRepository;
import com.example.appliancemgmt.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplianceService {

    @Autowired
    private ApplianceRepository applianceRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Appliance addAppliance(AddApplianceDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Appliance appliance = new Appliance();
        appliance.setName(dto.getName());
        appliance.setStatus(dto.getStatus() != null ? Appliance.Status.valueOf(dto.getStatus()) : null);
        appliance.setTestStartDate(dto.getTestStartDate());
        appliance.setTestEndDate(dto.getTestEndDate());
        appliance.setClient(client);

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

    public void deleteAppliance(Long id) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appliance not found"));
        applianceRepository.delete(appliance);
    }
}