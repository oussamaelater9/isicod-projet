package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.MarkAsReadRequest;
import com.example.appliancemgmt.entity.Notification;
import com.example.appliancemgmt.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Validated
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam Long userId) {
        logger.info("Fetching notifications for userId: {}", userId);
        try {
            List<Notification> notifications = notificationService.getNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            logger.error("Error fetching notifications for userId: {}, error={}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long id, @Valid @RequestBody MarkAsReadRequest request) {
        logger.info("Marking notification as read: id={}, request={}", id, request);
        try {
            if (request.getUserId() == null) {
                logger.warn("Invalid userId in request: {}", request);
                return ResponseEntity.badRequest().body("User ID cannot be null");
            }
            notificationService.markAsRead(id, request.getUserId());
            return ResponseEntity.ok("Notification marked as read");
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: id={}, userId={}, error={}", id, request.getUserId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error marking notification as read: id={}, userId={}, error={}", id, request.getUserId(), e.getMessage(), e);
            if (e.getCause() instanceof SQLException && e.getMessage().contains("constraint")) {
                logger.warn("Constraint violation for notification: id={}, userId={}, assuming invalid userId", id, request.getUserId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user ID: userId=" + request.getUserId());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to mark notification as read: " + e.getMessage());
        }
    }
}