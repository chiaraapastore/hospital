package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Medicinale;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.repository.MedicinaleRepository;
import hospitalApplication.repository.PazienteRepository;
import hospitalApplication.repository.UtenteRepository;
import hospitalApplication.models.Paziente;
import hospitalApplication.models.Utente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorService {

    private final DepartmentRepository departmentRepository;
    private final MedicinaleRepository medicinaleRepository;
    private final PazienteRepository pazienteRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;

    public DoctorService(DepartmentRepository departmentRepository, MedicinaleRepository medicinaleRepository, PazienteRepository pazienteRepository, AuthenticationService authenticationService, UtenteRepository utenteRepository) {
        this.departmentRepository = departmentRepository;
        this.medicinaleRepository = medicinaleRepository;
        this.pazienteRepository = pazienteRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
    }

    @Transactional
    public String visualizzaMedicineReparto(Long repartoId) {
        String username = authenticationService.getUsername();
        System.out.println("Username autenticato: " + username);
        Utente utente = utenteRepository.findByUsername(username);
        if (utente == null) {
            throw new IllegalArgumentException("Utente con username '" + username + "' non trovato nel database.");
        }


        List<Medicinale> medicinali = medicinaleRepository.findByDepartmentId(repartoId);

        return "Referenze disponibili per il reparto con ID " + repartoId + ": " + medicinali;
    }

    @Transactional
    public String somministraMedicine(Long pazienteId, Long medicinaleId) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Paziente paziente = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        Medicinale medicinale = medicinaleRepository.findById(medicinaleId)
                .orElseThrow(() -> new RuntimeException("Medicinale non trovato"));

        if (medicinale.getAvailableQuantity()<= 0) {
            throw new RuntimeException("Il medicinale non è più disponibile");
        }

        medicinale.setAvailableQuantity(medicinale.getAvailableQuantity() - 1);
        medicinaleRepository.save(medicinale);

        return "Medicine somministrata con successo al paziente " + paziente.getNome() +
                " con il medicinale " + medicinale.getNome();
    }
}
