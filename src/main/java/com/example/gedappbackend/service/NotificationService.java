package com.example.gedappbackend.service;

import com.example.gedappbackend.model.Notification;
import com.example.gedappbackend.model.NotificationType;
import com.example.gedappbackend.model.User;
import com.example.gedappbackend.repository.NotificationRepository;
import com.example.gedappbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MailService mailService;

    public Notification createNotification(UUID recipientId, NotificationType type,
                                           String title, String content, Map<String, String> metadata) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(type)
                .title(title)
                .content(content)
                .metadata(metadata)
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);

        // Send real-time notification via WebSocket
        try {
            messagingTemplate.convertAndSendToUser(
                    recipientId.toString(),
                    "/queue/notifications",
                    notification
            );
        } catch (Exception e) {
            log.warn("WebSocket notification failed: {}", e.getMessage());
        }

        // Send email for important notifications
        if (type == NotificationType.VALIDATION_REQUEST || type == NotificationType.MENTION) {
            mailService.sendNotificationEmail(recipient.getEmail(), title, content);
        }

        log.info("Notification sent to {}: {}", recipientId, title);

        return notification;
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(UUID userId) {
        return notificationRepository.findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(UUID notificationId, UUID userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (notification.getRecipient().getId().equals(userId)) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    public long countUnread(UUID userId) {
        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }
}
