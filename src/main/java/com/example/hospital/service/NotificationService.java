package com.example.hospital.service;

import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Notification;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.NotificationRepository;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;

    public NotificationService(NotificationRepository notificationRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService) {
        this.notificationRepository = notificationRepository;
        this.utenteRepository = utenteRepository;
        this.authenticationService = authenticationService;
    }

    public void sendWelcomeNotification(Utente newUser) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        String message = "Benvenuto, " + newUser.getFirstName() + "! Sei stato registrato come " + newUser.getRole() + ".";
        createAndSendNotification("Sistema", newUser.getId(), message, "welcome");
    }


    public void notifyNewPatient(String doctorId, String chiefId, String patientName) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        String message = "Un nuovo paziente, " + patientName + ", è stato assegnato al reparto.";

        createAndSendNotification("Sistema", doctorId, message, "new_patient");
        createAndSendNotification("Sistema", chiefId, message, "new_patient");
    }


    public void notifyDepartmentChange(String userId, String newDepartmentName, String chiefId) {
        Utente user = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        String message = "Sei stato spostato in un nuovo reparto! Ora fai parte del reparto " + newDepartmentName + ".";

        createAndSendNotification("Sistema", userId, message, "department_change");

        if (chiefId != null) {
            String chiefMessage = user.getFirstName() + " " + user.getLastName() + " è stato spostato nel reparto " + newDepartmentName + ".";
            createAndSendNotification("Sistema", chiefId, chiefMessage, "department_change");
        }
    }


    private void createAndSendNotification(String senderId, String receiverId, String message, String type) {
        Notification notification = new Notification();
        notification.setMessaggio(message);
        notification.setSenderId(senderId);
        notification.setReceiverId(receiverId);
        notification.setDataOra(LocalDateTime.now());
        notification.setType(type);
        notification.setLetta(false);

        notificationRepository.save(notification);

        Utente receiver = utenteRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        receiver.setCountNotification(receiver.getCountNotification() + 1);
        utenteRepository.save(receiver);
    }

    public void markNotificationAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notifica non trovata"));

        if (!notification.isLetta()) {
            notification.setLetta(true);
            notification.setReadNotification(LocalDateTime.now());
            notificationRepository.save(notification);

            Utente receiver = utenteRepository.findById(notification.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));

            int newCount = receiver.getCountNotification() > 0 ? receiver.getCountNotification() - 1 : 0;
            receiver.setCountNotification(newCount);
            utenteRepository.save(receiver);
        }
    }


    public void markAllNotificationsAsReadForUser(String userId) {
        Utente user = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        List<Notification> unreadNotifications = notificationRepository.findByReceiverIdAndLettaFalse(userId);

        if (!unreadNotifications.isEmpty()) {
            unreadNotifications.forEach(notification -> {
                notification.setLetta(true);
                notification.setReadNotification(LocalDateTime.now());
            });
            notificationRepository.saveAll(unreadNotifications);

            user.setCountNotification(0);
            utenteRepository.save(user);
        }
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByReceiverId(userId);
    }
}
