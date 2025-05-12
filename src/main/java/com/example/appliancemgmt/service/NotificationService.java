package com.example.appliancemgmt.service;

import com.example.appliancemgmt.entity.Notification;
import com.example.appliancemgmt.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notification createNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        return notification;
    }

    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findAll().stream()
                .peek(n -> n.getReadBy().contains(userId))
                .toList();
    }

    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.getReadBy().add(userId);
        notificationRepository.save(notification);
    }
}
