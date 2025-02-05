package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Department;
import hospitalApplication.models.Medicinale;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.repository.MedicinaleRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class HeadOfDepartmentService {
    private MedicinaleRepository medicinaleRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;

    public HeadOfDepartmentService(MedicinaleRepository medicinaleRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService) {
        this.medicinaleRepository = medicinaleRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
    }

    public String aggiornaScorteReparto(Long repartoId, Long medicinaleId, int nuovaQuantita) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Medicinale medicinale = medicinaleRepository.findById(medicinaleId)
                .orElseThrow(() -> new IllegalArgumentException("Medicinale non trovato"));

        if (!medicinale.getDepartment().getId().equals(repartoId)) {
            throw new IllegalArgumentException("Il medicinale non appartiene al reparto specificato");
        }

        medicinale.setQuantita(nuovaQuantita);
        medicinaleRepository.save(medicinale);
        return "Scorte aggiornate per la referenza " + medicinaleId + " nel reparto " + repartoId;
    }


    public String inviaNotifica(String repartoId, String messaggio) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        return "Notifica inviata al reparto " + repartoId + ": " + messaggio;
    }
}
