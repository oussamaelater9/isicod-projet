package com.example.appliancemgmt.service;

import com.example.appliancemgmt.entity.Notification;
import com.example.appliancemgmt.repository.NotificationRepository;
import com.example.appliancemgmt.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    public Notification createNotification(String message, Long senderId, Long clientId) {
        logger.info("Creating notification: message={}, senderId={}, clientId={}", message, senderId, clientId);
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSenderId(senderId);
        notification.setClientId(clientId);
        notification.setReadBy(new HashSet<>()); // Changed to Set
        try {
            notification = notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            logger.info("Notification created and sent: id={}", notification.getId());
        } catch (Exception e) {
            logger.error("Failed to create notification: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create notification", e);
        }
        return notification;
    }

    public List<Notification> getNotifications(Long userId) {
        logger.info("Fetching notifications for userId: {}", userId);
        return notificationRepository.findAll().stream()
                .map(n -> {
                    Notification notification = new Notification();
                    notification.setId(n.getId());
                    notification.setMessage(n.getMessage());
                    notification.setCreatedAt(n.getCreatedAt());
                    notification.setSenderId(n.getSenderId());
                    notification.setClientId(n.getClientId());
                    notification.setReadBy(n.getReadBy() != null ? n.getReadBy() : new HashSet<>()); // Changed to Set
                    return notification;
                })
                .toList();
    }

    @Transactional
    public void markAsRead(Long id, Long userId) {
        logger.info("Marking notification as read: id={}, userId={}", id, userId);
        if (!notificationRepository.existsById(id)) {
            logger.warn("Notification not found: id={}", id);
            throw new IllegalArgumentException("Notification not found: id=" + id);
        }
        if (userId == null || userId <= 0 || userId > Long.MAX_VALUE / 2) {
            logger.warn("Invalid userId: userId={}", userId);
            throw new IllegalArgumentException("Invalid userId: userId=" + userId);
        }
        if (!userRepository.existsById(userId)) {
            logger.warn("User not found: userId={}", userId);
            throw new IllegalArgumentException("User not found: userId=" + userId);
        }

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Notification not found after exists check: id={}", id);
                    return new IllegalArgumentException("Notification not found: id=" + id);
                });

        Set<Long> readBy = notification.getReadBy() != null ? notification.getReadBy() : new HashSet<>();
        if (!readBy.contains(userId)) {
            logger.info("Adding userId={} to readBy for notification id={}", userId, id);
            readBy.add(userId);
            notification.setReadBy(readBy); // Update the readBy Set
            try {
                notificationRepository.save(notification);
                logger.info("Notification marked as read successfully: id={}", id);
            } catch (Exception e) {
                logger.error("Failed to update notification_read_by: id={}, userId={}, error={}", id, userId, e.getMessage(), e);
                throw new IllegalStateException("Failed to update notification_read_by: " + e.getMessage(), e);
            }
        } else {
            logger.info("Notification already marked as read by userId={}", userId);
        }
    }

    public void deleteNotification(Long notificationId) {
        logger.info("Deleting notification: id={}", notificationId);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> {
                    logger.warn("Notification not found: id={}", notificationId);
                    return new IllegalArgumentException("Notification not found: ID " + notificationId);
                });
        try {
            notificationRepository.delete(notification);
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            logger.info("Notification deleted successfully: id={}", notificationId);
        } catch (Exception e) {
            logger.error("Failed to delete notification: id={}, error={}", notificationId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete notification", e);
        }
    }
}
