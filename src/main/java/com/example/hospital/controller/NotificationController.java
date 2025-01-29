package com.example.hospital.controller;

import com.example.hospital.models.Notification;
import com.example.hospital.models.Utente;
import com.example.hospital.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/welcome/{userId}")
    public ResponseEntity<Void> sendWelcomeNotification(@PathVariable Utente userId) {
        notificationService.sendWelcomeNotification(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new-patient")
    public ResponseEntity<Void> notifyNewPatient(@RequestParam String doctorId, @RequestParam String chiefId, @RequestParam String patientName) {
        notificationService.notifyNewPatient(doctorId, chiefId, patientName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/department-change")
    public ResponseEntity<Void> notifyDepartmentChange(@RequestParam String userId, @RequestParam String newDepartmentName, @RequestParam(required = false) String chiefId) {
        notificationService.notifyDepartmentChange(userId, newDepartmentName, chiefId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/mark-read/{notificationId}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable String notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/mark-all-read/{userId}")
    public ResponseEntity<Void> markAllNotificationsAsReadForUser(@PathVariable String userId) {
        notificationService.markAllNotificationsAsReadForUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
}
