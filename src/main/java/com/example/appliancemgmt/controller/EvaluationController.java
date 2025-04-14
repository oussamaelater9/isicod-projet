package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.AddEvaluationDTO;
import com.example.appliancemgmt.dto.EvaluationResponseDTO;
import com.example.appliancemgmt.entity.Evaluation;
import com.example.appliancemgmt.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<List<EvaluationResponseDTO>> getAllEvaluations() {
        List<EvaluationResponseDTO> evaluations = evaluationService.getAllEvaluations();
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponseDTO> getEvaluationById(@PathVariable Long id) {
        return evaluationService.getEvaluationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/appliance/{applianceId}")
    public ResponseEntity<List<EvaluationResponseDTO>> getEvaluationsByApplianceId(@PathVariable Long applianceId) {
        List<EvaluationResponseDTO> evaluations = evaluationService.getEvaluationsByApplianceId(applianceId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<EvaluationResponseDTO>> getEvaluationsByClientId(@PathVariable Long clientId) {
        List<EvaluationResponseDTO> evaluations = evaluationService.getEvaluationsByClientId(clientId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/outcome/{outcome}")
    public ResponseEntity<List<EvaluationResponseDTO>> getEvaluationsByOutcome(@PathVariable String outcome) {
        List<EvaluationResponseDTO> evaluations = evaluationService.getEvaluationsByOutcome(outcome);
        return ResponseEntity.ok(evaluations);
    }

    @PostMapping
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody AddEvaluationDTO dto) {
        try {
            Evaluation evaluation = evaluationService.addEvaluation(dto);
            return ResponseEntity.status(201).body(evaluation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable Long id, @RequestBody AddEvaluationDTO dto) {
        try {
            Evaluation updatedEvaluation = evaluationService.updateEvaluation(id, dto);
            return ResponseEntity.ok(updatedEvaluation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        try {
            evaluationService.deleteEvaluation(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}