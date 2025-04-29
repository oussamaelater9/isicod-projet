package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.AddSessionDTO;
import com.example.appliancemgmt.dto.ApiResponse;
import com.example.appliancemgmt.dto.SessionResponseDTO;
import com.example.appliancemgmt.entity.Session;
import com.example.appliancemgmt.entity.SessionStatus;
import com.example.appliancemgmt.service.SessionService;
import com.example.appliancemgmt.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private LogService logService;

    @GetMapping
    public ApiResponse<List<SessionResponseDTO>> getSessions() {
        List<SessionResponseDTO> sessions = sessionService.findAll();
        return new ApiResponse<>(HttpStatus.OK.value(), "Sessions retrieved successfully", sessions);
    }

    @GetMapping("/{id}")
    public ApiResponse<Session> getSessionById(@PathVariable Long id) {
        Session session = sessionService.getSessionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with id: " + id));
        return new ApiResponse<>(HttpStatus.OK.value(), "Session retrieved successfully", session);
    }

    @GetMapping("/appliance/{applianceId}")
    public ApiResponse<List<Session>> getSessionsByApplianceId(@PathVariable Long applianceId) {
        List<Session> sessions = sessionService.getSessionsByApplianceId(applianceId);
        return new ApiResponse<>(HttpStatus.OK.value(), "Sessions retrieved successfully", sessions);
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<Session>> getSessionsByStatus(@PathVariable SessionStatus status) {
        List<Session> sessions = sessionService.getSessionsByStatus(status);
        return new ApiResponse<>(HttpStatus.OK.value(), "Sessions retrieved successfully", sessions);
    }

    @PostMapping
    public ApiResponse<Session> createSession(@RequestBody AddSessionDTO sessionDTO, @RequestParam Long applianceId) {
        Session createdSession = sessionService.createSession(sessionDTO, applianceId);
        logService.logAction("CREATE", "Session", "Created session with ID: " + createdSession.getId() + " for appliance ID: " + applianceId);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Session created successfully", createdSession);
    }

    @PutMapping("/{id}")
    public ApiResponse<Session> updateSession(@PathVariable Long id, @RequestBody Session session,
                                              @RequestParam(required = false) Long applianceId) {
        Session updatedSession = sessionService.updateSession(id, session, applianceId);
        logService.logAction("UPDATE", "Session", "Updated session with ID: " + id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Session updated successfully", updatedSession);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        logService.logAction("DELETE", "Session", "Deleted session with ID: " + id);
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Session deleted successfully", null);
    }
}