package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.AddApplianceDTO;
import com.example.appliancemgmt.dto.ApplianceResponseDTO;
import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.service.ApplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appliances")
public class ApplianceController {

    @Autowired
    private ApplianceService applianceService;

    @PostMapping
    public ResponseEntity<Appliance> addAppliance(@RequestBody AddApplianceDTO dto) {
        try {
            Appliance appliance = applianceService.addAppliance(dto);
            return ResponseEntity.status(201).body(appliance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ApplianceResponseDTO>> getAllAppliances() {
        List<ApplianceResponseDTO> appliances = applianceService.getAllAppliances();
        return ResponseEntity.ok(appliances);
    }
}