package hospitalApplication.controller;

import hospitalApplication.models.Department;
import hospitalApplication.models.DoctorDTO;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.repository.UtenteRepository;
import hospitalApplication.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    public AdminController(AdminService adminService, DepartmentRepository departmentRepository, UtenteRepository utenteRepository) {
        this.adminService = adminService;
        this.departmentRepository = departmentRepository;
        this.utenteRepository = utenteRepository;
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





    @PostMapping("/assegna-capo-reparto")
    public ResponseEntity<String> assegnaCapoReparto(@RequestParam String nomeUtente, @RequestParam Long repartoId) {
        String response = adminService.assegnaCapoReparto(nomeUtente, repartoId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/assegna-dottore-reparto/{utenteId}/{repartoId}")
    public ResponseEntity<String> assegnaDottoreAReparto(@PathVariable Long utenteId, @PathVariable Long repartoId) {
        if (utenteId == null || repartoId == null) {
            return ResponseEntity.badRequest().body("Errore: Parametri mancanti.");
        }

        Optional<Utente> dottore = utenteRepository.findById(utenteId);
        if (dottore.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Dottore non trovato.");
        }

        Optional<Department> reparto = departmentRepository.findById(repartoId);
        if (reparto.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Reparto non trovato.");
        }

        Utente user = dottore.get();
        user.setReparto(reparto.get());
        utenteRepository.save(user);

        return ResponseEntity.ok("Dottore assegnato con successo al reparto.");
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
    public ResponseEntity<String> creaDottore(@RequestBody Map<String, String> payload) {
        String firstName = payload.get("firstName");
        String lastName = payload.get("lastName");
        String email = payload.get("email");
        String repartoNome = payload.get("repartoNome");

        if (firstName == null || lastName == null || email == null || repartoNome == null ||
                firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || repartoNome.isEmpty()) {
            return ResponseEntity.badRequest().body("Tutti i campi sono obbligatori, incluso il reparto.");
        }

        Optional<Department> repartoOpt = departmentRepository.findFirstByNome(repartoNome);
        if (repartoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Il reparto specificato non esiste.");
        }
        String response = adminService.creaDottore(firstName, lastName, email, repartoOpt.get().getNome());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/crea-capo-reparto")
    public ResponseEntity<String> creaCapoReparto(@RequestBody Map<String, String> payload) {
        String firstName = payload.get("firstName");
        String lastName = payload.get("lastName");
        String email = payload.get("email");
        String repartoNome = payload.get("repartoNome");

        if (firstName == null || lastName == null || email == null || repartoNome == null ||
                firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || repartoNome.isEmpty()) {
            return ResponseEntity.badRequest().body("Tutti i campi sono obbligatori, incluso il reparto.");
        }

        Optional<Department> repartoOpt = departmentRepository.findFirstByNome(repartoNome);
        if (repartoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Il reparto specificato non esiste.");
        }

        String response = adminService.creaCapoReparto(firstName, lastName, email, repartoOpt.get());
        return ResponseEntity.ok(response);
    }




}

