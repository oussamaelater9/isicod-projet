package com.example.appliancemgmt.repository;

import com.example.appliancemgmt.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByApplianceId(Long applianceId);

    @Query("SELECT e FROM Evaluation e WHERE e.appliance.client.id = :clientId")
    List<Evaluation> findByApplianceClientId(Long clientId);

    List<Evaluation> findByOutcome(String outcome);
}