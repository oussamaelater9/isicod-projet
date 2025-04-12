package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.entity.ApplianceStatus;
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

    @GetMapping
    public ResponseEntity<List<Appliance>> getAllAppliances() {
        List<Appliance> appliances = applianceService.getAllAppliances();
        return ResponseEntity.ok(appliances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appliance> getApplianceById(@PathVariable Long id) {
        return applianceService.getApplianceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Appliance>> getAppliancesByClientId(@PathVariable Long clientId) {
        List<Appliance> appliances = applianceService.getAppliancesByClientId(clientId);
        return ResponseEntity.ok(appliances);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Appliance>> getAppliancesByStatus(@PathVariable ApplianceStatus status) {
        List<Appliance> appliances = applianceService.getAppliancesByStatus(status);
        return ResponseEntity.ok(appliances);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Appliance>> searchAppliancesByName(@RequestParam String name) {
        List<Appliance> appliances = applianceService.searchAppliancesByName(name);
        return ResponseEntity.ok(appliances);
    }

    @PostMapping
    public ResponseEntity<Appliance> createAppliance(@RequestBody Appliance appliance, @RequestParam Long clientId) {
        try {
            Appliance createdAppliance = applianceService.createAppliance(appliance, clientId);
            return ResponseEntity.status(201).body(createdAppliance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appliance> updateAppliance(@PathVariable Long id, @RequestBody Appliance appliance,
                                                     @RequestParam(required = false) Long clientId) {
        try {
            Appliance updatedAppliance = applianceService.updateAppliance(id, appliance, clientId);
            return ResponseEntity.ok(updatedAppliance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppliance(@PathVariable Long id) {
        try {
            applianceService.deleteAppliance(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}