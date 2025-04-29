package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.AddApplianceDTO;
import com.example.appliancemgmt.dto.ApplianceResponseDTO;
import com.example.appliancemgmt.dto.ApiResponse;
import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.service.ApplianceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appliances")
public class ApplianceController {
    private static final Logger logger = LoggerFactory.getLogger(ApplianceController.class);

    @Autowired
    private ApplianceService applianceService;

    @PostMapping
    public ApiResponse<ApplianceResponseDTO> addAppliance(@Valid @RequestBody AddApplianceDTO dto) {
        try {
            Appliance appliance = applianceService.addAppliance(dto);
            ApplianceResponseDTO responseDTO = new ApplianceResponseDTO();
            responseDTO.setId(appliance.getId());
            responseDTO.setName(appliance.getName());
            responseDTO.setStatus(appliance.getStatus().name());
            responseDTO.setTestStartDate(appliance.getTestStartDate());
            responseDTO.setTestEndDate(appliance.getTestEndDate());
            ApplianceResponseDTO.ClientSummaryDTO clientDto = new ApplianceResponseDTO.ClientSummaryDTO();
            clientDto.setId(appliance.getClient().getId());
            clientDto.setCompanyName(appliance.getClient().getCompanyName());
            clientDto.setName(appliance.getClient().getName());
            responseDTO.setClient(clientDto);
            return new ApiResponse<>(HttpStatus.CREATED.value(), "Appliance created successfully", responseDTO);
        } catch (IllegalArgumentException e) {
            logger.error("Appliance creation failed: {}", e.getMessage());
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    @GetMapping
    public ApiResponse<List<ApplianceResponseDTO>> getAllAppliances() {
        try {
            List<ApplianceResponseDTO> appliances = applianceService.getAllAppliances();
            return new ApiResponse<>(HttpStatus.OK.value(), "Appliances fetched successfully", appliances);
        } catch (Exception e) {
            logger.error("Error fetching appliances: {}", e.getMessage(), e);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch appliances: " + e.getMessage(), null);
        }
    }
}