package com.example.hospital.repository;

import com.example.hospital.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByReceiverIdAndLettaFalse(String receiverId);
    List<Notification> findByReceiverId(String userId);
}

