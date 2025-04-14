package com.example.appliancemgmt.repository;

import com.example.appliancemgmt.entity.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplianceRepository extends JpaRepository<Appliance, Long> {
}