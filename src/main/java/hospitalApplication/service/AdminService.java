package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Department;
import hospitalApplication.models.Paziente;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.PazienteRepository;
import hospitalApplication.repository.UtenteRepository;
import hospitalApplication.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class AdminService {

    private final UtenteRepository utenteRepository;
    private final DepartmentRepository departmentRepository;
    private final PazienteRepository pazienteRepository;

    private final AuthenticationService authenticationService;

    public AdminService(UtenteRepository utenteRepository, DepartmentRepository departmentRepository, AuthenticationService authenticationService, PazienteRepository pazienteRepository) {
        this.departmentRepository = departmentRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.pazienteRepository = pazienteRepository;
    }

    public String creaReparto(String nomeReparto) {
        System.out.println("Creazione reparto: " + nomeReparto);
        if (nomeReparto == null || nomeReparto.isEmpty()) {
            throw new IllegalArgumentException("Il nome del reparto non può essere vuoto");
        }

        Department reparto = new Department();
        reparto.setNome(nomeReparto);
        departmentRepository.save(reparto);

        return "Reparto " + nomeReparto + " creato con successo";
    }



    public String aggiungiDottoreAReparto(Long utenteId, Long repartoId) {

        Utente utenteAutenticato = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utenteAutenticato == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }

        Utente dottore = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Dottore non trovato con ID: " + utenteId));

        if (!"DOCTOR".equals(dottore.getRole())) {
            throw new IllegalArgumentException("L'utente con ID " + utenteId + " non è un dottore");
        }

        Department reparto = departmentRepository.findById(repartoId)
                .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato con ID: " + repartoId));

        dottore.setReparto(reparto);

        utenteRepository.save(dottore);

        return "Dottore " + dottore.getFirstName() + " " + dottore.getLastName() + " assegnato al reparto " + reparto.getNome();
    }



    public String assegnaCapoReparto(String nomeUtente, Long repartoId) {
        String authenticatedUsername = authenticationService.getUsername();
        System.out.println("Username autenticato: " + authenticatedUsername);

        Utente authenticatedUser = utenteRepository.findByUsername(authenticatedUsername);

        if (authenticatedUser == null) {
            throw new IllegalArgumentException("Utente autenticato non trovato nel database: " + authenticatedUsername);
        }

        System.out.println("Utente autenticato trovato: " + authenticatedUser.getUsername());
        System.out.println("Assegnazione capo reparto per: " + nomeUtente + " al reparto ID: " + repartoId);

        String[] parts = nomeUtente.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Il nome utente deve contenere almeno nome e cognome");
        }
        String firstName = parts[0];
        String lastName = parts[1];

        Utente utente = utenteRepository.findByFirstNameAndLastName(firstName, lastName);
        if (utente == null) {
            throw new IllegalArgumentException("Utente con nome " + firstName + " " + lastName + " non trovato nel database");
        }

        Department reparto = departmentRepository.findById(repartoId)
                .orElseThrow(() -> new IllegalArgumentException("Reparto con ID " + repartoId + " non trovato"));

        reparto.setCapoReparto(utente);
        departmentRepository.save(reparto);

        return "Utente " + utente.getFirstName() + " " + utente.getLastName() + " assegnato come capo del reparto " + reparto.getNome();
    }





    public String assegnaPazienteAReparto(Long pazienteId, Long repartoId) {
        Paziente paziente = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paziente non trovato con ID: " + pazienteId));

        Department reparto = departmentRepository.findById(repartoId)
                .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato con ID: " + repartoId));

        paziente.setReparto(reparto);

        pazienteRepository.save(paziente);

        return "Paziente con ID " + pazienteId + " assegnato al reparto " + reparto.getNome();
    }

}
