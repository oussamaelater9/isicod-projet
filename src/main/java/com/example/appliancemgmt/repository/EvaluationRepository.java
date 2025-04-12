package com.example.appliancemgmt.repository;

import com.example.appliancemgmt.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByApplianceId(Long applianceId);
    List<Evaluation> findByClientId(Long clientId);
    List<Evaluation> findByOutcome(String outcome);
}