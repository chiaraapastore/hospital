package com.example.hospital.service;

import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Notification;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.NotificationRepository;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {


    private final NotificationRepository notificationRepository;
    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;

    public NotificationService(NotificationRepository notificationRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService) {
        this.utenteRepository = utenteRepository;
        this.authenticationService = authenticationService;
        this.notificationRepository = notificationRepository;
    }

    //todo invio notifica, ricevo notifica, per ogni operazione che faccio devo ritornare il messaggio tipo utente, contatore a 0

    public List<Notification> getNotificationsForUser(String userId) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return notificationRepository.findByUserIdAndLettaFalse(userId);
    }

    public void markNotificationAsRead(String notificationId) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notifica non trovata"));
        notification.setLetta(true);
        notificationRepository.save(notification);
    }
}

