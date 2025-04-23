package com.example.appliancemgmt.service;

import com.example.appliancemgmt.dto.AddEvaluationDTO;
import com.example.appliancemgmt.dto.EvaluationResponseDTO;
import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.entity.Evaluation;
import com.example.appliancemgmt.entity.EvaluationOutcome;
import com.example.appliancemgmt.repository.ApplianceRepository;
import com.example.appliancemgmt.repository.EvaluationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final ApplianceRepository applianceRepository;

    public EvaluationService(EvaluationRepository evaluationRepository, ApplianceRepository applianceRepository) {
        this.evaluationRepository = evaluationRepository;
        this.applianceRepository = applianceRepository;
    }

    public List<EvaluationResponseDTO> findAll() {
        return evaluationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EvaluationResponseDTO> findById(Long id) {
        return evaluationRepository.findById(id).map(this::mapToDTO);
    }

    public List<EvaluationResponseDTO> findByApplianceId(Long applianceId) {
        return evaluationRepository.findByApplianceId(applianceId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public EvaluationResponseDTO save(AddEvaluationDTO dto) {
        Appliance appliance = applianceRepository.findById(dto.getApplianceId())
                .orElseThrow(() -> new IllegalArgumentException("Appliance not found with id: " + dto.getApplianceId()));

        Evaluation evaluation = new Evaluation();
        evaluation.setMeetingDate(dto.getMeetingDate());
        evaluation.setOutcome(EvaluationOutcome.valueOf(dto.getOutcome()));
        evaluation.setNotes(dto.getNotes());
        evaluation.setAppliance(appliance);

        Evaluation saved = evaluationRepository.save(evaluation);
        return mapToDTO(saved);
    }

    public EvaluationResponseDTO update(Long id, AddEvaluationDTO dto) {
        return evaluationRepository.findById(id)
                .map(existing -> {
                    Appliance appliance = applianceRepository.findById(dto.getApplianceId())
                            .orElseThrow(() -> new IllegalArgumentException("Appliance not found with id: " + dto.getApplianceId()));
                    existing.setMeetingDate(dto.getMeetingDate());
                    existing.setOutcome(EvaluationOutcome.valueOf(dto.getOutcome()));
                    existing.setNotes(dto.getNotes());
                    existing.setAppliance(appliance);
                    return mapToDTO(evaluationRepository.save(existing));
                })
                .orElseThrow(() -> new IllegalArgumentException("Evaluation not found with id: " + id));
    }

    public void delete(Long id) {
        if (!evaluationRepository.existsById(id)) {
            throw new IllegalArgumentException("Evaluation not found with id: " + id);
        }
        evaluationRepository.deleteById(id);
    }

    private EvaluationResponseDTO mapToDTO(Evaluation evaluation) {
        EvaluationResponseDTO.ApplianceSummaryDTO applianceDTO = new EvaluationResponseDTO.ApplianceSummaryDTO();
        applianceDTO.setId(evaluation.getAppliance().getId());
        applianceDTO.setName(evaluation.getAppliance().getName());

        EvaluationResponseDTO dto = new EvaluationResponseDTO();
        dto.setId(evaluation.getId());
        dto.setMeetingDate(evaluation.getMeetingDate());
        dto.setOutcome(evaluation.getOutcome() != null ? evaluation.getOutcome().name() : null);
        dto.setNotes(evaluation.getNotes());
        dto.setCreatedAt(evaluation.getCreatedAt());
        dto.setAppliance(applianceDTO);
        return dto;
    }
}