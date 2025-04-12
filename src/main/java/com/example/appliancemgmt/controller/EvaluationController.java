package com.example.appliancemgmt.controller;

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
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable Long id) {
        return evaluationService.getEvaluationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/appliance/{applianceId}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByApplianceId(@PathVariable Long applianceId) {
        List<Evaluation> evaluations = evaluationService.getEvaluationsByApplianceId(applianceId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByClientId(@PathVariable Long clientId) {
        List<Evaluation> evaluations = evaluationService.getEvaluationsByClientId(clientId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/outcome/{outcome}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByOutcome(@PathVariable String outcome) {
        List<Evaluation> evaluations = evaluationService.getEvaluationsByOutcome(outcome);
        return ResponseEntity.ok(evaluations);
    }

    @PostMapping
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody Evaluation evaluation,
                                                       @RequestParam Long applianceId,
                                                       @RequestParam Long clientId) {
        try {
            Evaluation createdEvaluation = evaluationService.createEvaluation(evaluation, applianceId, clientId);
            return ResponseEntity.status(201).body(createdEvaluation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable Long id,
                                                       @RequestBody Evaluation evaluation,
                                                       @RequestParam(required = false) Long applianceId,
                                                       @RequestParam(required = false) Long clientId) {
        try {
            Evaluation updatedEvaluation = evaluationService.updateEvaluation(id, evaluation, applianceId, clientId);
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