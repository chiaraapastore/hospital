package hospitalApplication.controller;

import hospitalApplication.models.Notification;
import hospitalApplication.models.Utente;
import hospitalApplication.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/welcome/{userId}")
    public ResponseEntity<String> sendWelcomeNotification(@PathVariable Long userId) {
        Utente user = notificationService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        }
        try {
            notificationService.sendWelcomeNotification(user);
            return ResponseEntity.ok("Notifica di benvenuto inviata con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'invio della notifica: " + e.getMessage());
        }
    }

    @PostMapping("/new-patient")
    public ResponseEntity<Void> notifyNewPatient(@RequestParam Long doctorId, @RequestParam Long chiefId, @RequestParam String patientName) {
        Utente doctor = notificationService.getUserById(doctorId);
        Utente chief = notificationService.getUserById(chiefId);
        notificationService.notifyNewPatient(doctor, chief, patientName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/department-change")
    public ResponseEntity<Void> notifyDepartmentChange(@RequestParam Long userId, @RequestParam String newDepartmentName, @RequestParam(required = false) Long chiefId) {
        Utente user = notificationService.getUserById(userId);
        Utente chief = chiefId != null ? notificationService.getUserById(chiefId) : null;
        notificationService.notifyDepartmentChange(user, newDepartmentName, chief);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/mark-read/{notificationId}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/mark-all-read/{userId}")
    public ResponseEntity<Void> markAllNotificationsAsReadForUser(@PathVariable Long userId) {
        Utente user = notificationService.getUserById(userId);
        notificationService.markAllNotificationsAsReadForUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        Utente user = notificationService.getUserById(userId);
        List<Notification> notifications = notificationService.getUserNotifications(user);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody Notification notification) {
        notificationService.sendNotification(notification);
        return ResponseEntity.ok("Notifica inviata con successo.");
    }

    @GetMapping("/capo-reparto/{chiefId}")
    public ResponseEntity<List<Notification>> getNotificationsForChief(@PathVariable Long chiefId) {
        Utente chief = notificationService.getUserById(chiefId);
        if (chief == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Notification> notifications = notificationService.getNotificationsFromDoctors(chief);
        return ResponseEntity.ok(notifications);
    }

}
