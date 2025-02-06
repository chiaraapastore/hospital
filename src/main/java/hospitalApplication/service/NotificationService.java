package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Notification;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.NotificationRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void sendWelcomeNotification(Utente newUser) {
        Utente sender = utenteRepository.findByUsername(authenticationService.getUsername());
        if (sender == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        String message = "Benvenuto, " + newUser.getFirstName() + "! Sei stato registrato come " + newUser.getRole() + ".";
        createAndSendNotification(sender, newUser, message, "welcome");
    }

    @Transactional
    public void notifyNewPatient(Utente doctor, Utente chief, String patientName) {
        Utente sender = utenteRepository.findByUsername(authenticationService.getUsername());
        if (sender == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        String message = "Un nuovo paziente, " + patientName + ", è stato assegnato al reparto.";
        createAndSendNotification(sender, doctor, message, "new_patient");
        createAndSendNotification(sender, chief, message, "new_patient");
    }

    @Transactional
    public void notifyDepartmentChange(Utente user, String newDepartmentName, Utente chief) {
        Utente sender = utenteRepository.findByUsername(authenticationService.getUsername());
        if (sender == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        String message = "Sei stato spostato in un nuovo reparto! Ora fai parte del reparto " + newDepartmentName + ".";
        createAndSendNotification(sender, user, message, "department_change");

        if (chief != null) {
            String chiefMessage = user.getFirstName() + " " + user.getLastName() + " è stato spostato nel reparto " + newDepartmentName + ".";
            createAndSendNotification(sender, chief, chiefMessage, "department_change");
        }
    }

    private void createAndSendNotification(Utente sender, Utente receiver, String message, String type) {
        Notification notification = new Notification();
        notification.setMessaggio(message);
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setDataOra(LocalDateTime.now());
        notification.setType(type);
        notification.setLetta(false);
        notificationRepository.save(notification);

        receiver.setCountNotification(receiver.getCountNotification() + 1);
        utenteRepository.save(receiver);
    }

    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notifica non trovata"));

        if (!notification.isLetta()) {
            notification.setLetta(true);
            notification.setReadNotification(LocalDateTime.now());
            notificationRepository.save(notification);

            Utente receiver = notification.getReceiver();
            int newCount = Math.max(0, receiver.getCountNotification() - 1);
            receiver.setCountNotification(newCount);
            utenteRepository.save(receiver);
        }
    }

    @Transactional
    public void markAllNotificationsAsReadForUser(Utente user) {
        List<Notification> unreadNotifications = notificationRepository.findByReceiverIdAndLettaFalse(user.getId());

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

    @Transactional
    public List<Notification> getUserNotifications(Utente user) {
        return notificationRepository.findByReceiverId(user.getId());
    }

    @Transactional
    public Utente getUserById(Long userId) {
        return utenteRepository.findById(userId)
                .orElse(null);
    }

}
