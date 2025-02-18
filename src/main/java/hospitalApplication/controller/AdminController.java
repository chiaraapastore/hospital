package hospitalApplication.controller;

import hospitalApplication.models.Department;
import hospitalApplication.models.DoctorDTO;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.repository.UtenteRepository;
import hospitalApplication.service.AdminService;
import hospitalApplication.service.KeycloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/admin")
public class AdminController {


    private final AdminService adminService;
    private final DepartmentRepository departmentRepository;
    private final UtenteRepository utenteRepository;
    private final KeycloakService keycloakService;

    public AdminController(AdminService adminService, DepartmentRepository departmentRepository, UtenteRepository utenteRepository, KeycloakService keycloakService) {
        this.adminService = adminService;
        this.departmentRepository = departmentRepository;
        this.utenteRepository = utenteRepository;
        this.keycloakService = keycloakService;
    }


    @PostMapping("/crea-reparto")
    public ResponseEntity<Map<String, String>> creaReparto(@RequestBody Map<String, String> payload) {
        String repartoNome = payload.get("repartoNome");


        if (repartoNome == null || repartoNome.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Il nome del reparto è obbligatorio."));
        }

        Optional<Department> existingReparto = departmentRepository.findFirstByNome(repartoNome);
        if (existingReparto.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Il reparto esiste già!"));
        }
        Department nuovoReparto = new Department();
        nuovoReparto.setNome(repartoNome);
        departmentRepository.save(nuovoReparto);

        return ResponseEntity.ok(Map.of("message", "Reparto aggiunto con successo!"));
    }





    @PutMapping("/assegna-capo-reparto")
    public ResponseEntity<Map<String, String>> assegnaCapoReparto(@RequestBody Map<String, Long> payload) {
        Long utenteId = payload.get("utenteId");
        Long repartoId = payload.get("repartoId");

        System.out.println("API ricevuta: Assegna capo reparto ID " + utenteId + " al reparto ID " + repartoId);

        if (utenteId == null || repartoId == null) {
            System.out.println("Errore: Parametri mancanti!");
            return ResponseEntity.badRequest().body(Map.of("error", "Parametri mancanti."));
        }

        Optional<Utente> userOpt = utenteRepository.findById(utenteId);
        Optional<Department> departmentOpt = departmentRepository.findById(repartoId);

        if (userOpt.isEmpty()) {
            System.out.println("Errore: Utente non trovato!");
            return ResponseEntity.badRequest().body(Map.of("error", "Utente non trovato."));
        }

        if (departmentOpt.isEmpty()) {
            System.out.println("Errore: Reparto non trovato!");
            return ResponseEntity.badRequest().body(Map.of("error", "Reparto non trovato."));
        }

        Utente utente = userOpt.get();
        Department nuovoReparto = departmentOpt.get();

        Optional<Department> repartoAttualeOpt = departmentRepository.findByCapoReparto(utente);
        if (repartoAttualeOpt.isPresent()) {
            Department repartoAttuale = repartoAttualeOpt.get();
            repartoAttuale.setCapoReparto(null);
            departmentRepository.save(repartoAttuale);
            System.out.println("Capo rimosso dal vecchio reparto " + repartoAttuale.getNome());
        }

        nuovoReparto.setCapoReparto(utente);
        departmentRepository.save(nuovoReparto);

        System.out.println("Capo reparto aggiornato con successo: " + utente.getFirstName() + " → " + nuovoReparto.getNome());

        return ResponseEntity.ok(Map.of("message", "Capo reparto assegnato con successo!"));
    }



    @PutMapping("/assegna-dottore-reparto/{utenteId}/{repartoId}")
    public ResponseEntity<String> assegnaDottoreAReparto(@PathVariable Long utenteId, @PathVariable Long repartoId) {
        System.out.println("Ricevuta richiesta: Cambio reparto per dottore ID " + utenteId + " → Reparto ID " + repartoId);

        Optional<Utente> dottoreOpt = utenteRepository.findById(utenteId);
        Optional<Department> repartoOpt = departmentRepository.findById(repartoId);

        if (dottoreOpt.isEmpty()) {
            System.out.println("Errore: Dottore non trovato.");
            return ResponseEntity.badRequest().body("Errore: Dottore non trovato.");
        }

        if (repartoOpt.isEmpty()) {
            System.out.println("Errore: Reparto non trovato.");
            return ResponseEntity.badRequest().body("Errore: Reparto non trovato.");
        }

        Utente dottore = dottoreOpt.get();
        Department reparto = repartoOpt.get();

        System.out.println("Reparto attuale: " + (dottore.getReparto() != null ? dottore.getReparto().getNome() : "Nessuno"));
        System.out.println("Nuovo reparto assegnato: " + reparto.getNome());

        dottore.setReparto(reparto);
        utenteRepository.saveAndFlush(dottore);


        Optional<Utente> updatedDottore = utenteRepository.findById(utenteId);
        System.out.println(" Reparto aggiornato nel DB: " + (updatedDottore.get().getReparto() != null ? updatedDottore.get().getReparto().getNome() : "Nessuno"));

        return ResponseEntity.ok("Dottore assegnato al reparto " + reparto.getNome());
    }





    @GetMapping("/dottori")
    public ResponseEntity<List<DoctorDTO>> getAllDottori() {
        return ResponseEntity.ok(adminService.getAllDottori());
    }

    @GetMapping("/reparti")
    public ResponseEntity<List<Department>> getAllReparti() {
        return ResponseEntity.ok(adminService.getAllReparti());
    }

    @GetMapping("/capo-reparti")
    public ResponseEntity<List<DoctorDTO>> getCapoReparti() {
        return ResponseEntity.ok(adminService.getCapoReparti());
    }


    @PostMapping("/crea-dottore")
    public ResponseEntity<Map<String, String>> creaDottore(@RequestBody Map<String, String> payload) {
        String firstName = payload.get("firstName");
        String lastName = payload.get("lastName");
        String email = payload.get("email");
        String repartoNome = payload.get("repartoNome");


        if (firstName == null || lastName == null || email == null || repartoNome == null ||
                firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || repartoNome.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tutti i campi sono obbligatori, incluso il reparto."));
        }

        Optional<Department> repartoOpt = departmentRepository.findFirstByNome(repartoNome);
        if (repartoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Errore: Il reparto specificato non esiste."));
        }

        Department reparto = repartoOpt.get();


        Utente dottore = new Utente();
        dottore.setFirstName(firstName);
        dottore.setLastName(lastName);
        dottore.setEmail(email);
        dottore.setRole("dottore");
        dottore.setReparto(reparto);

        utenteRepository.save(dottore);

        return ResponseEntity.ok(Map.of("message", "Dottore creato con successo e assegnato al reparto " + reparto.getNome()));

    }



    @PostMapping("/crea-capo-reparto")
    public ResponseEntity<Map<String, String>> creaCapoReparto(@RequestBody Map<String, String> payload) {
        System.out.println("Richiesta ricevuta: " + payload);

        String firstName = payload.get("firstName");
        String lastName = payload.get("lastName");
        String email = payload.get("email");
        String repartoNome = payload.get("repartoNome");

        if (firstName == null || lastName == null || email == null || repartoNome == null ||
                firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || repartoNome.isEmpty()) {
            System.out.println("Errore: Campi mancanti!");
            return ResponseEntity.badRequest().body(Map.of("error", "Tutti i campi sono obbligatori, incluso il reparto."));
        }

        Optional<Department> repartoOpt = departmentRepository.findFirstByNome(repartoNome);
        if (repartoOpt.isEmpty()) {
            System.out.println("Errore: Il reparto '" + repartoNome + "' non esiste!");
            return ResponseEntity.badRequest().body(Map.of("error", "Errore: Il reparto specificato non esiste."));
        }

        Department reparto = repartoOpt.get();
        String response = adminService.creaCapoReparto(firstName, lastName, email, reparto);
        return ResponseEntity.ok(Map.of("message", response));
    }




}

