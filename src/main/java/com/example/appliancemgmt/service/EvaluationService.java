package com.example.appliancemgmt.service;

import com.example.appliancemgmt.dto.AddEvaluationDTO;
import com.example.appliancemgmt.dto.EvaluationResponseDTO;
import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.entity.Evaluation;
import com.example.appliancemgmt.repository.ApplianceRepository;
import com.example.appliancemgmt.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    public Evaluation addEvaluation(AddEvaluationDTO dto) {
        Appliance appliance = applianceRepository.findById(dto.getApplianceId())
                .orElseThrow(() -> new IllegalArgumentException("Appliance not found"));

        Evaluation evaluation = new Evaluation();
        evaluation.setMeetingDate(dto.getMeetingDate());
        evaluation.setOutcome(dto.getOutcome());
        evaluation.setNotes(dto.getNotes());
        evaluation.setAppliance(appliance);

        return evaluationRepository.save(evaluation);
    }

    public List<EvaluationResponseDTO> getAllEvaluations() {
        return evaluationRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<EvaluationResponseDTO> getEvaluationById(Long id) {
        return evaluationRepository.findById(id).map(this::toResponseDTO);
    }

    public List<EvaluationResponseDTO> getEvaluationsByApplianceId(Long applianceId) {
        return evaluationRepository.findByApplianceId(applianceId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<EvaluationResponseDTO> getEvaluationsByClientId(Long clientId) {
        return evaluationRepository.findByApplianceClientId(clientId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<EvaluationResponseDTO> getEvaluationsByOutcome(String outcome) {
        return evaluationRepository.findByOutcome(outcome).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Evaluation updateEvaluation(Long id, AddEvaluationDTO dto) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluation not found"));

        Appliance appliance = applianceRepository.findById(dto.getApplianceId())
                .orElseThrow(() -> new IllegalArgumentException("Appliance not found"));

        evaluation.setMeetingDate(dto.getMeetingDate());
        evaluation.setOutcome(dto.getOutcome());
        evaluation.setNotes(dto.getNotes());
        evaluation.setAppliance(appliance);

        return evaluationRepository.save(evaluation);
    }

    public void deleteEvaluation(Long id) {
        if (!evaluationRepository.existsById(id)) {
            throw new IllegalArgumentException("Evaluation not found");
        }
        evaluationRepository.deleteById(id);
    }

    private EvaluationResponseDTO toResponseDTO(Evaluation evaluation) {
        EvaluationResponseDTO dto = new EvaluationResponseDTO();
        dto.setId(evaluation.getId());
        dto.setMeetingDate(evaluation.getMeetingDate());
        dto.setOutcome(evaluation.getOutcome());
        dto.setNotes(evaluation.getNotes());
        dto.setCreatedAt(evaluation.getCreatedAt());
        EvaluationResponseDTO.ApplianceSummaryDTO applianceDto = new EvaluationResponseDTO.ApplianceSummaryDTO();
        applianceDto.setId(evaluation.getAppliance().getId());
        applianceDto.setName(evaluation.getAppliance().getName());
        dto.setAppliance(applianceDto);
        return dto;
    }
}