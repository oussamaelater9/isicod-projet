package com.example.appliancemgmt.service;

import com.example.appliancemgmt.entity.Notification;
import com.example.appliancemgmt.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notification createNotification(String message, Long senderId, Long clientId) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSenderId(senderId);
        notification.setClientId(clientId);
        notification.setReadBy(new ArrayList<>());
        notification = notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        return notification;
    }

    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findAll().stream()
                .map(n -> {
                    Notification notification = new Notification();
                    notification.setId(n.getId());
                    notification.setMessage(n.getMessage());
                    notification.setCreatedAt(n.getCreatedAt());
                    notification.setSenderId(n.getSenderId());
                    notification.setClientId(n.getClientId());
                    notification.setReadBy(n.getReadBy());
                    return notification;
                })
                .toList();
    }

    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.getReadBy().add(userId);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
