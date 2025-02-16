package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Department;
import hospitalApplication.models.Medicinale;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.repository.MedicinaleRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class HeadOfDepartmentService {
    private MedicinaleRepository medicinaleRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;
    private final DepartmentRepository departmentRepository;

    public HeadOfDepartmentService(MedicinaleRepository medicinaleRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService, DepartmentRepository departmentRepository) {
        this.medicinaleRepository = medicinaleRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
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

    @Transactional
    public String inviaNotifica(String repartoId, String messaggio) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        return "Notifica inviata al reparto " + repartoId + ": " + messaggio;
    }


    @Transactional
    public void cambiaReparto(Long doctorId, Long nuovoRepartoId) {
        Utente dottore = utenteRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Dottore non trovato"));
        Department reparto = departmentRepository.findById(nuovoRepartoId).orElseThrow(() -> new IllegalArgumentException("Reparto non trovato"));
        dottore.setReparto(reparto);
        utenteRepository.save(dottore);
    }

    @Transactional
    public void assegnaFerie(Long doctorId, Date dataFerie) {
        Utente dottore = utenteRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Dottore non trovato"));
        dottore.setFerie(dataFerie);
        utenteRepository.save(dottore);
    }


    @Transactional
    public void assegnaTurno(Long doctorId, String turno) {
        Utente dottore = utenteRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Dottore non trovato"));
        dottore.setTurno(turno);
        utenteRepository.save(dottore);
    }


    @Transactional(readOnly = true)
    public List<Department> getReparti() {
        return departmentRepository.findAll();
    }

    @Transactional
    public List<String> getFerieDisponibili() {
        return Arrays.asList(
                "2025-07-01", "2025-07-02", "2025-07-03",
                "2025-08-10", "2025-08-11", "2025-08-12",
                "2025-12-24", "2025-12-25", "2025-12-26"
        );
    }
}
