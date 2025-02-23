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
        Utente sender = getAuthenticatedUser();
        String message = "Benvenuto, " + newUser.getFirstName() + "! Sei stato registrato come " + newUser.getRole() + ".";
        createAndSendNotification(sender, newUser, message, "welcome");
    }

    @Transactional
    public void notifyNewPatient(Utente doctor, Utente chief, String patientName) {
        Utente sender = getAuthenticatedUser();
        String message = "Un nuovo paziente, " + patientName + ", è stato assegnato al reparto.";
        createAndSendNotification(sender, doctor, message, "new_patient");
        if (chief != null) {
            createAndSendNotification(sender, chief, message, "new_patient");
        }
    }

    @Transactional
    public void notifyDepartmentChange(Utente user, String newDepartmentName, Utente chief) {
        Utente sender = getAuthenticatedUser();
        String message = "Sei stato spostato in un nuovo reparto! Ora fai parte del reparto " + newDepartmentName + ".";
        createAndSendNotification(sender, user, message, "department_change");
        if (chief != null) {
            String chiefMessage = user.getFirstName() + " " + user.getLastName() + " è stato spostato nel reparto " + newDepartmentName + ".";
            createAndSendNotification(sender, chief, chiefMessage, "department_change");
        }
    }

    @Transactional
    public void sendNotification(Utente receiver, String message, String type) {
        Utente sender = getAuthenticatedUser();
        createAndSendNotification(sender, receiver, message, type);
    }

    private void createAndSendNotification(Utente sender, Utente receiver, String message, String type) {
        if (receiver == null) {
            return;
        }
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
    public void markAllNotificationsAsRead() {
        Utente user = getAuthenticatedUser();
        List<Notification> unreadNotifications = notificationRepository.findByReceiverIdAndLettaFalse(user.getId());
        if (!unreadNotifications.isEmpty()) {
            unreadNotifications.forEach(notification -> notification.setLetta(false));
            notificationRepository.saveAll(unreadNotifications);
            user.setCountNotification(0);
            utenteRepository.save(user);
        }
    }

    @Transactional
    public void sendNotificationAdmin(Long adminId, String message) {
        Utente sender = getAuthenticatedUser();
        Utente admin = utenteRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Amministratore non trovato"));
        createAndSendNotification(sender, admin, message, "admin_notification");
    }

    @Transactional
    public void sendNotificationCapoReparto(Long chiefId, String message) {
        Utente sender = getAuthenticatedUser();
        Utente chief = utenteRepository.findById(chiefId)
                .orElseThrow(() -> new IllegalArgumentException("Capo reparto non trovato"));
        createAndSendNotification(sender, chief, message, "capo_reparto_notification");
    }

    @Transactional
    public List<Notification> getUserNotifications() {
        Utente user = getAuthenticatedUser();
        return notificationRepository.findByReceiverId(user.getId());
    }

    private Utente getAuthenticatedUser() {
        Utente user = utenteRepository.findByUsername(authenticationService.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        return user;
    }

    public Utente getUserById(Long userId) {
        return utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
    }

    public List<Notification> getNotificationsFromDoctors(Utente chief) {
        return notificationRepository.findByRecipientAndSenderRole(chief, "dottore");
    }

}
