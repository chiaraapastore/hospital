package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Turni;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.TurnoRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class TurniService {


    private final TurnoRepository turnoRepository;
    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;
    private final NotificationService notificationService;

    public TurniService(TurnoRepository turnoRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService, NotificationService notificationService) {
        this.turnoRepository = turnoRepository;
        this.utenteRepository = utenteRepository;
        this.authenticationService = authenticationService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Turni assegnaTurno(Long dottoreId, LocalDate inizioTurno, LocalDate fineTurno) {
        Utente capoReparto = getAuthenticatedUser();
        Utente dottore = utenteRepository.findById(dottoreId).orElseThrow(() -> new IllegalArgumentException("Dottore non trovato"));
        Turni turno = new Turni(inizioTurno, fineTurno, capoReparto);
        notificationService.sendNotificationTurni(dottore, capoReparto, inizioTurno, fineTurno);
        return turnoRepository.save(turno);
    }

    private Utente getAuthenticatedUser() {
        Utente user = utenteRepository.findByUsername(authenticationService.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        return user;
    }
}
