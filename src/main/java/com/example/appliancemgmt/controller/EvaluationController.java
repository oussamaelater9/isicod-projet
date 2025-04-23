package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.AddEvaluationDTO;
import com.example.appliancemgmt.dto.ApiResponse;
import com.example.appliancemgmt.dto.EvaluationResponseDTO;
import com.example.appliancemgmt.service.EvaluationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {
    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping
    public ApiResponse<List<EvaluationResponseDTO>> getEvaluations() {
        List<EvaluationResponseDTO> evaluations = evaluationService.findAll();
        return new ApiResponse<>(HttpStatus.OK.value(), "Evaluations retrieved successfully", evaluations);
    }

    @GetMapping("/{id}")
    public ApiResponse<EvaluationResponseDTO> getEvaluation(@PathVariable Long id) {
        EvaluationResponseDTO evaluation = evaluationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluation not found with id: " + id));
        return new ApiResponse<>(HttpStatus.OK.value(), "Evaluation retrieved successfully", evaluation);
    }

    @GetMapping("/appliance/{applianceId}")
    public ApiResponse<List<EvaluationResponseDTO>> getEvaluationsByAppliance(@PathVariable Long applianceId) {
        List<EvaluationResponseDTO> evaluations = evaluationService.findByApplianceId(applianceId);
        return new ApiResponse<>(HttpStatus.OK.value(), "Evaluations for appliance retrieved successfully", evaluations);
    }

    @PostMapping
    public ApiResponse<EvaluationResponseDTO> addEvaluation(@RequestBody AddEvaluationDTO dto) {
        EvaluationResponseDTO evaluation = evaluationService.save(dto);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Evaluation added successfully", evaluation);
    }

    @PutMapping("/{id}")
    public ApiResponse<EvaluationResponseDTO> updateEvaluation(@PathVariable Long id, @RequestBody AddEvaluationDTO dto) {
        EvaluationResponseDTO evaluation = evaluationService.update(id, dto);
        return new ApiResponse<>(HttpStatus.OK.value(), "Evaluation updated successfully", evaluation);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.delete(id);
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Evaluation deleted successfully", null);
    }
}