package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Ferie;
import hospitalApplication.models.Turni;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.FerieRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class FerieService {

    private final FerieRepository ferieRepository;
    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;
    private final NotificationService notificationService;

    public FerieService(FerieRepository ferieRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService, NotificationService notificationService) {
        this.ferieRepository = ferieRepository;
        this.utenteRepository = utenteRepository;
        this.authenticationService = authenticationService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Ferie assegnaFerie(Long capoRepartoId,LocalDate inizioFerie, LocalDate fineFerie) {
        Utente dottore = getAuthenticatedUser();
        Utente capoReparto = utenteRepository.findById(capoRepartoId).orElseThrow(() -> new IllegalArgumentException("CapoReparto non trovato"));
        Ferie ferie = new Ferie(inizioFerie, fineFerie, dottore);
        notificationService.sendNotificationTurni(dottore, capoReparto, inizioFerie, fineFerie);
        return ferieRepository.save(ferie);
    }


    private Utente getAuthenticatedUser() {
        Utente user = utenteRepository.findByUsername(authenticationService.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        return user;
    }
}
