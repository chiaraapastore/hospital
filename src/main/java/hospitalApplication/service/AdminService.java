package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Department;
import hospitalApplication.models.DoctorDTO;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.PazienteRepository;
import hospitalApplication.repository.UtenteRepository;
import hospitalApplication.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional
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


    @Transactional
    public String aggiungiDottoreAReparto(Long utenteId, Long repartoId) {

        Utente utenteAutenticato = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utenteAutenticato == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }

        Utente dottore = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Dottore non trovato con ID: " + utenteId));

        if (!"dottore".equals(dottore.getRole())) {
            throw new IllegalArgumentException("L'utente con ID " + utenteId + " non è un dottore");
        }

        Department reparto = departmentRepository.findById(repartoId)
                .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato con ID: " + repartoId));

        dottore.setReparto(reparto);

        utenteRepository.save(dottore);

        return "Dottore " + dottore.getFirstName() + " " + dottore.getLastName() + " assegnato al reparto " + reparto.getNome();
    }

    @Transactional
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
        utente.setReparto(reparto);
        departmentRepository.save(reparto);
        utenteRepository.save(utente);
        departmentRepository.save(reparto);

        return "Utente " + utente.getFirstName() + " " + utente.getLastName() + " assegnato come capo del reparto " + reparto.getNome();
    }

    @Transactional
    public String assegnaDottoreAReparto(Long utenteId, Long repartoId) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + utenteId));

        if (!utente.getRole().equals("dottore")) {
            throw new IllegalArgumentException("L'utente con ID " + utenteId + " non è un dottore.");
        }

        Department reparto = departmentRepository.findById(repartoId)
                .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato con ID: " + repartoId));

        utente.setReparto(reparto);

        utenteRepository.save(utente);

        return "Dottore con ID " + utenteId + " assegnato al reparto " + reparto.getNome();
    }

    @Transactional
    public List<DoctorDTO> getAllDottori() {
        return utenteRepository.findAll().stream()
                .filter(utente -> "dottore".equalsIgnoreCase(utente.getRole()))
                .map(utente -> new DoctorDTO(
                        utente.getId(),
                        utente.getFirstName(),
                        utente.getLastName(),
                        utente.getEmail(),
                        utente.getNumeroMatricola(),
                        utente.getReparto() != null ? utente.getReparto().getNome() : "Nessun reparto"
                ))
                .collect(Collectors.toList());
    }



    @Transactional
    public List<Department> getAllReparti() {
        return departmentRepository.findAll();
    }


    @Transactional
    public List<DoctorDTO> getCapoReparti() {
        return utenteRepository.findAll().stream()
                .filter(utente -> "capo-reparto".equalsIgnoreCase(utente.getRole()))
                .map(utente -> new DoctorDTO(
                        utente.getId(),
                        utente.getFirstName(),
                        utente.getLastName(),
                        utente.getEmail(),
                        utente.getNumeroMatricola(),
                        (utente.getReparto() != null) ? utente.getReparto().getNome() : "Nessun reparto"
                ))
                .collect(Collectors.toList());
    }



    @Transactional
    public String creaDottore(String firstName, String lastName, String email, Department reparto) {
        if (reparto == null) {
            reparto = departmentRepository.findByNome("Medicina Generale");
            if (reparto == null) {
                reparto = new Department();
                reparto.setNome("Medicina Generale");
                departmentRepository.save(reparto);
            }
        }

        Utente dottore = new Utente();
        dottore.setFirstName(firstName);
        dottore.setLastName(lastName);
        dottore.setEmail(email);
        dottore.setRole("dottore");
        dottore.setReparto(reparto);

        utenteRepository.save(dottore);
        return "Dottore creato con successo e assegnato al reparto " + reparto.getNome();
    }



    @Transactional
    public String creaCapoReparto(String firstName, String lastName, String email, Department reparto) {
        if (reparto == null) {
            reparto = departmentRepository.findByNome("Medicina Generale");
            if (reparto == null) {
                reparto = new Department();
                reparto.setNome("Medicina Generale");
                departmentRepository.save(reparto);
            }
        }

        Utente capoReparto = new Utente();
        capoReparto.setFirstName(firstName);
        capoReparto.setLastName(lastName);
        capoReparto.setEmail(email);
        capoReparto.setRole("capoReparto");
        capoReparto.setReparto(reparto);

        utenteRepository.save(capoReparto);
        return "Capo Reparto creato con successo e assegnato al reparto " + reparto.getNome();
    }


}
