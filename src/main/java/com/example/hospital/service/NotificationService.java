package com.example.hospital.service;

import com.example.hospital.models.Notification;
import com.example.hospital.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {


    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationsForUser(String userId) {
        return notificationRepository.findByUserIdAndLettaFalse(userId);
    }

    public void markNotificationAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notifica non trovata"));
        notification.setLetta(true);
        notificationRepository.save(notification);
    }
}

