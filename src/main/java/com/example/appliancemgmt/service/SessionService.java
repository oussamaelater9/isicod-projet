package com.example.appliancemgmt.service;

import com.example.appliancemgmt.dto.AddSessionDTO;
import com.example.appliancemgmt.dto.SessionsResponseDTO;
import com.example.appliancemgmt.entity.Appliance;
import com.example.appliancemgmt.entity.Session;
import com.example.appliancemgmt.entity.SessionStatus;
import com.example.appliancemgmt.repository.ApplianceRepository;
import com.example.appliancemgmt.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    public List<SessionsResponseDTO> findAll() {
        return sessionRepository.findAll().stream()
                .map(session -> new SessionsResponseDTO(
                        session.getId(),
                        session.getApplianceId(),
                        session.getStatus(),
                        session.getStartTime(),
                        session.getNotes()
                ))
                .collect(Collectors.toList());
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public List<Session> getSessionsByApplianceId(Long applianceId) {
        return sessionRepository.findByApplianceId(applianceId);
    }

    public List<Session> getSessionsByStatus(SessionStatus status) {
        return sessionRepository.findByStatus(status);
    }

    public Session createSession(AddSessionDTO sessionDTO, Long applianceId) {
        if (sessionDTO.getSessionDate() == null || sessionDTO.getStatus() == null) {
            throw new IllegalArgumentException("Session date and status are required");
        }
        Optional<Appliance> appliance = applianceRepository.findById(applianceId);
        if (!appliance.isPresent()) {
            throw new IllegalArgumentException("Appliance not found with id: " + applianceId);
        }

        Session session = new Session();
        session.setSessionDate(sessionDTO.getSessionDate());
        session.setStatus(sessionDTO.getStatus());
        session.setNotes(sessionDTO.getNotes());
        session.setAppliance(appliance.get());

        return sessionRepository.save(session);
    }

    public Session updateSession(Long id, Session updatedSession, Long applianceId) {
        return sessionRepository.findById(id)
                .map(existing -> {
                    existing.setSessionDate(updatedSession.getSessionDate());
                    existing.setStatus(updatedSession.getStatus());
                    existing.setNotes(updatedSession.getNotes());
                    if (applianceId != null) {
                        Appliance appliance = applianceRepository.findById(applianceId)
                                .orElseThrow(() -> new IllegalArgumentException("Appliance not found with id: " + applianceId));
                        existing.setAppliance(appliance);
                    }
                    return sessionRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Session not found with id: " + id));
    }

    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new IllegalArgumentException("Session not found with id: " + id);
        }
        sessionRepository.deleteById(id);
    }
}