package com.example.appliancemgmt.service;

import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.entity.Client;
import com.example.appliancemgmt.entity.Evaluation;
import com.example.appliancemgmt.repository.ApplianceRepository;
import com.example.appliancemgmt.repository.ClientRepository;
import com.example.appliancemgmt.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EvaluationService {
    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    @Autowired
    private ClientRepository clientRepository;

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    public Optional<Evaluation> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }

    public List<Evaluation> getEvaluationsByApplianceId(Long applianceId) {
        return evaluationRepository.findByApplianceId(applianceId);
    }

    public List<Evaluation> getEvaluationsByClientId(Long clientId) {
        return evaluationRepository.findByClientId(clientId);
    }

    public List<Evaluation> getEvaluationsByOutcome(String outcome) {
        return evaluationRepository.findByOutcome(outcome);
    }

    public Evaluation createEvaluation(Evaluation evaluation, Long applianceId, Long clientId) {
        if (evaluation.getMeetingDate() == null || evaluation.getOutcome() == null) {
            throw new IllegalArgumentException("Meeting date and outcome are required");
        }
        Optional<Appliance> appliance = applianceRepository.findById(applianceId);
        if (appliance.isPresent()) {
            evaluation.setAppliance(appliance.get());
        } else {
            throw new IllegalArgumentException("Appliance not found with id: " + applianceId);
        }
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isPresent()) {
            evaluation.setClient(client.get());
        } else {
            throw new IllegalArgumentException("Client not found with id: " + clientId);
        }
        return evaluationRepository.save(evaluation);
    }

    public Evaluation updateEvaluation(Long id, Evaluation updatedEvaluation, Long applianceId, Long clientId) {
        return evaluationRepository.findById(id)
                .map(existing -> {
                    existing.setMeetingDate(updatedEvaluation.getMeetingDate());
                    existing.setOutcome(updatedEvaluation.getOutcome());
                    existing.setNotes(updatedEvaluation.getNotes());
                    if (applianceId != null) {
                        Appliance appliance = applianceRepository.findById(applianceId)
                                .orElseThrow(() -> new IllegalArgumentException("Appliance not found with id: " + applianceId));
                        existing.setAppliance(appliance);
                    }
                    if (clientId != null) {
                        Client client = clientRepository.findById(clientId)
                                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));
                        existing.setClient(client);
                    }
                    return evaluationRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Evaluation not found with id: " + id));
    }

    public void deleteEvaluation(Long id) {
        if (!evaluationRepository.existsById(id)) {
            throw new IllegalArgumentException("Evaluation not found with id: " + id);
        }
        evaluationRepository.deleteById(id);
    }
}