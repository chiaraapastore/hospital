package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Department;
import hospitalApplication.models.Medicinale;
import hospitalApplication.models.Notification;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.NotificationRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public void sendWelcomeNotification(Utente utenteAdmin, Utente newUser) {
        String message = "Benvenuto, " + newUser.getFirstName() + "! Sei stato registrato come " + newUser.getRole() + ".";
        createAndSendNotification(utenteAdmin, newUser, message, "welcome");
    }


    @Transactional
    public void sendNotificationAssignToDepartment(Utente newUser, String nomeReparto) {
        Utente sender = getAuthenticatedUser();
        String message = "Benvenuto, " + newUser.getFirstName() + "! Sei stato assegnato al reparto " + nomeReparto + ".";
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
    public List<Notification> markAllNotificationsAsRead() {
        Utente user = getAuthenticatedUser();

        List<Notification> unreadNotifications = notificationRepository.findByReceiverIdAndLettaFalse(user.getId());

        if (!unreadNotifications.isEmpty()) {

            unreadNotifications.forEach(notification -> notification.setLetta(true));
            notificationRepository.saveAll(unreadNotifications);

            user.setCountNotification(0);
            utenteRepository.save(user);
        }

        return notificationRepository.findByReceiverId(user.getId());
    }


    @Transactional
    public void sendDoctorNotificationToCapoReparto(Utente dottore, Utente capoReparto, String nomeFarmaco) {
        String message = "Attenzione, il farmaco"+nomeFarmaco+" è scaduto";
        createAndSendNotification(dottore, capoReparto, message, "farmaco_scaduto");
    }


    @Transactional
    public void sendNotificationSomministration(Utente dottore, Utente capoReparto, String nomeFarmaco){
        String message = "Il dottore"+dottore.getFirstName()+" "+dottore.getLastName()+"ha somministrato il farmaco"+nomeFarmaco+"al paziente";
        createAndSendNotification(dottore,capoReparto, message, "farmaco");

    }

    @Transactional
    public void sendNotificationFerie(Utente dottore, Utente capoReparto, LocalDate dataInizioFerie, LocalDate dataFineFerie){
        String message = "Il sottoscritto"+dottore.getFirstName()+" "+dottore.getLastName()+"sarà assente dal giorno"+dataInizioFerie+"al giorno"+dataFineFerie;
        createAndSendNotification(dottore,capoReparto, message, "ferie");
    }

    @Transactional
    public void sendNotificationTurni(Utente dottore, Utente capoReparto, LocalDate dataInizioFerie, LocalDate dataFineFerie){
        String message = "Il sottoscritto"+dottore.getFirstName()+" "+dottore.getLastName()+"dovrà coprire i turni dal giorno"+dataInizioFerie+"al giorno"+dataFineFerie;
        createAndSendNotification(dottore,capoReparto, message, "turni");
    }

    private Utente getAuthenticatedUser() {
        Utente user = utenteRepository.findByUsername(authenticationService.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        return user;
    }


}
