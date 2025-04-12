package com.example.appliancemgmt.repository;

import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.entity.ApplianceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplianceRepository extends JpaRepository<Appliance, Long> {
    List<Appliance> findByClientId(Long clientId);
    List<Appliance> findByStatus(ApplianceStatus status);
    List<Appliance> findByNameContainingIgnoreCase(String name);
}