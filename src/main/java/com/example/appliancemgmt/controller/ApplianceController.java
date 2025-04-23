package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.AddApplianceDTO;
import com.example.appliancemgmt.dto.ApiResponse;
import com.example.appliancemgmt.dto.ApplianceResponseDTO;
import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.service.ApplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appliances")
public class ApplianceController {

    @Autowired
    private ApplianceService applianceService;

    @PostMapping
    public ApiResponse<Appliance> addAppliance(@RequestBody AddApplianceDTO dto) {
        Appliance appliance = applianceService.addAppliance(dto);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Appliance added successfully", appliance);
    }

    @GetMapping
    public ApiResponse<List<ApplianceResponseDTO>> getAllAppliances() {
        List<ApplianceResponseDTO> appliances = applianceService.getAllAppliances();
        return new ApiResponse<>(HttpStatus.OK.value(), "Appliances retrieved successfully", appliances);
    }
}