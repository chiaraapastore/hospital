package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Department;
import hospitalApplication.models.Paziente;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.repository.PazienteRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PazienteService {

    private final PazienteRepository pazienteRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;
    private final DepartmentRepository departmentRepository;

    public PazienteService(PazienteRepository pazienteRepository, AuthenticationService authenticationService, UtenteRepository utenteRepository, DepartmentRepository departmentRepository) {
        this.pazienteRepository = pazienteRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.departmentRepository = departmentRepository;
    }

    public Optional<Paziente> getPazienteById(Long id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return pazienteRepository.findById(id);
    }

    public Paziente savePaziente(Paziente paziente) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return pazienteRepository.save(paziente);
    }

    public void deletePaziente(Long id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        pazienteRepository.deleteById(id);
    }

    public String assegnaPazienteAReparto(Long pazienteId, Long repartoId) {
        Paziente paziente = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paziente non trovato"));
        Department reparto = departmentRepository.findById(repartoId)
                .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato"));

        paziente.setReparto(reparto);
        pazienteRepository.save(paziente);

        return "Paziente " + paziente.getId() + " assegnato al reparto " + reparto.getNome();
    }

}
