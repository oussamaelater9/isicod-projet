package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.AddSessionDTO;
import com.example.appliancemgmt.entity.Session;
import com.example.appliancemgmt.entity.SessionStatus;
import com.example.appliancemgmt.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return sessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/appliance/{applianceId}")
    public ResponseEntity<List<Session>> getSessionsByApplianceId(@PathVariable Long applianceId) {
        List<Session> sessions = sessionService.getSessionsByApplianceId(applianceId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Session>> getSessionsByStatus(@PathVariable SessionStatus status) {
        List<Session> sessions = sessionService.getSessionsByStatus(status);
        return ResponseEntity.ok(sessions);
    }

    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody AddSessionDTO sessionDTO, @RequestParam Long applianceId) {
        try {
            Session createdSession = sessionService.createSession(sessionDTO, applianceId);
            return ResponseEntity.status(201).body(createdSession);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session session,
                                                 @RequestParam(required = false) Long applianceId) {
        try {
            Session updatedSession = sessionService.updateSession(id, session, applianceId);
            return ResponseEntity.ok(updatedSession);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        try {
            sessionService.deleteSession(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}