package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.*;
import hospitalApplication.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class AdminService {

    private final UtenteRepository utenteRepository;
    private final DepartmentRepository departmentRepository;
    private final PazienteRepository pazienteRepository;
    private final MedicinaleRepository medicinaleRepository;
    private final MagazineRepository magazineRepository;

    private final AuthenticationService authenticationService;

    public AdminService(UtenteRepository utenteRepository, DepartmentRepository departmentRepository, AuthenticationService authenticationService, PazienteRepository pazienteRepository, MedicinaleRepository medicinaleRepository, MagazineRepository magazineRepository) {
        this.departmentRepository = departmentRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.pazienteRepository = pazienteRepository;
        this.medicinaleRepository = medicinaleRepository;
        this.magazineRepository = magazineRepository;
    }
    @Transactional
    public String creaReparto(String repartoNome) {
        Optional<Department> existingReparti = departmentRepository.findFirstByNome(repartoNome);
        if (!existingReparti.isEmpty()) {
            return "Errore: Il reparto esiste già!";
        }

        Department reparto = new Department();
        reparto.setNome(repartoNome);
        departmentRepository.save(reparto);

        return "Reparto aggiunto con successo!";
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
    public void assegnaDottoreAReparto(Long utenteId, Long repartoId) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Dottore non trovato con ID: " + utenteId));

        Department reparto = departmentRepository.findById(repartoId)
                .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato con ID: " + repartoId));

        utenteRepository.aggiornaReparto(utenteId, reparto);
        utenteRepository.flush();

        System.out.println("Dottore aggiornato a reparto: " + reparto.getNome());
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
    public String creaDottore(String firstName, String lastName, String email, String repartoNome) {
        Optional<Department> repartoOpt = departmentRepository.findFirstByNome(repartoNome);

        if (repartoOpt.isEmpty()) {
            throw new IllegalArgumentException("Errore: Il reparto specificato non esiste nel database.");
        }

        Department reparto = repartoOpt.get();

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
        Utente capoReparto = new Utente();
        capoReparto.setFirstName(firstName);
        capoReparto.setLastName(lastName);
        capoReparto.setEmail(email);
        capoReparto.setRole("capoReparto");
        capoReparto.setReparto(reparto);
        utenteRepository.save(capoReparto);
        return "Capo Reparto creato con successo e assegnato al reparto " + reparto.getNome();
    }


    @Transactional
    public String aggiungiFarmaco(Map<String, Object> payload) {
        String nome = (String) payload.get("nome");
        Integer quantita = (Integer) payload.get("quantita");
        Integer availableQuantity = (Integer) payload.get("availableQuantity");
        Integer puntoRiordino = (Integer) payload.get("puntoRiordino");
        String scadenza = (String) payload.get("scadenza");
        String categoria = (String) payload.get("categoria");
        String descrizione = (String) payload.get("descrizione");
        Long departmentId = payload.get("departmentId") != null ? Long.valueOf(payload.get("departmentId").toString()) : null;
        Long magazineId = payload.get("magazineId") != null ? Long.valueOf(payload.get("magazineId").toString()) : null;

        if (nome == null || nome.isEmpty() || quantita == null || quantita <= 0) {
            throw new IllegalArgumentException("Nome del farmaco e quantità devono essere validi.");
        }

        Medicinale nuovoFarmaco = new Medicinale();
        nuovoFarmaco.setNome(nome);
        nuovoFarmaco.setQuantita(quantita);
        nuovoFarmaco.setAvailableQuantity(availableQuantity);
        nuovoFarmaco.setPuntoRiordino(puntoRiordino);
        nuovoFarmaco.setScadenza(scadenza);
        nuovoFarmaco.setCategoria(categoria);
        nuovoFarmaco.setDescrizione(descrizione);

        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Dipartimento non trovato"));
            nuovoFarmaco.setDepartment(department);
        }

        if (magazineId != null) {
            Magazine magazine = magazineRepository.findById(magazineId)
                    .orElseThrow(() -> new IllegalArgumentException("Magazzino non trovato"));
            nuovoFarmaco.setMagazine(magazine);
        }

        medicinaleRepository.save(nuovoFarmaco);
        return "Farmaco aggiunto con successo!";
    }

}
