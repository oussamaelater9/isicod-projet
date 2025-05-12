package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.entity.Notification;
import com.example.appliancemgmt.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getNotifications(@RequestParam Long userId) {
        return notificationService.getNotifications(userId);
    }

    @PostMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id, @RequestBody Long userId) {
        notificationService.markAsRead(id, userId);
    }
}
