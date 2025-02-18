package hospitalApplication.controller;

import hospitalApplication.models.*;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.repository.MagazineRepository;
import hospitalApplication.repository.UtenteRepository;
import hospitalApplication.service.AdminService;
import hospitalApplication.service.KeycloakService;
import hospitalApplication.service.OrdineService;
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
    private final MagazineRepository magazineRepository;
    private final OrdineService ordineService;

    public AdminController(AdminService adminService, DepartmentRepository departmentRepository, UtenteRepository utenteRepository, KeycloakService keycloakService, OrdineService ordineService,MagazineRepository magazineRepository) {
        this.adminService = adminService;
        this.departmentRepository = departmentRepository;
        this.utenteRepository = utenteRepository;
        this.keycloakService = keycloakService;
        this.magazineRepository = magazineRepository;
        this.ordineService = ordineService;
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
            return ResponseEntity.badRequest().body(Map.of("error", "Parametri mancanti."));
        }

        Optional<Utente> userOpt = utenteRepository.findById(utenteId);
        Optional<Department> departmentOpt = departmentRepository.findById(repartoId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Utente non trovato."));
        }

        if (departmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Reparto non trovato."));
        }

        Utente utente = userOpt.get();
        Department nuovoReparto = departmentOpt.get();

        Optional<Department> repartoAttualeOpt = departmentRepository.findByCapoReparto(utente);
        repartoAttualeOpt.ifPresent(repartoAttuale -> {
            repartoAttuale.setCapoReparto(null);
            departmentRepository.save(repartoAttuale);
        });

        nuovoReparto.setCapoReparto(utente);
        utente.setReparto(nuovoReparto);
        utenteRepository.save(utente);
        departmentRepository.save(nuovoReparto);

        System.out.println("Capo reparto aggiornato con successo: " + utente.getFirstName() + " → " + nuovoReparto.getNome());

        return ResponseEntity.ok(Map.of("message", "Capo reparto assegnato con successo!"));
    }




    @PutMapping("/assegna-dottore-reparto/{utenteId}/{repartoId}")
    public ResponseEntity<Map<String, String>> assegnaDottoreAReparto(@PathVariable Long utenteId, @PathVariable Long repartoId) {
        System.out.println("Ricevuta richiesta: Cambio reparto per dottore ID " + utenteId + " → Reparto ID " + repartoId);

        Optional<Utente> dottoreOpt = utenteRepository.findById(utenteId);
        Optional<Department> repartoOpt = departmentRepository.findById(repartoId);

        if (dottoreOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Dottore non trovato."));
        }

        if (repartoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Reparto non trovato."));
        }

        Utente dottore = dottoreOpt.get();
        Department reparto = repartoOpt.get();

        dottore.setReparto(reparto);
        utenteRepository.saveAndFlush(dottore);

        System.out.println("Dottore aggiornato al reparto: " + reparto.getNome());

        return ResponseEntity.ok(Map.of("message", "Dottore assegnato al reparto " + reparto.getNome()));
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



    @PostMapping("/aggiungi-farmaco")
    public ResponseEntity<Map<String, String>> aggiungiFarmaco(@RequestBody Map<String, Object> payload) {
        String result = adminService.aggiungiFarmaco(payload);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @GetMapping("/magazzini")
    public ResponseEntity<List<Magazine>> getAllMagazzini() {
        List<Magazine> magazzini = magazineRepository.findAll();
        return ResponseEntity.ok(magazzini);
    }

    @GetMapping("/emergenze")
    public ResponseEntity<List<Medicinale>> getEmergenze() {
        return ResponseEntity.ok(adminService.getEmergenze());
    }


    @GetMapping("/ordini")
    public ResponseEntity<List<Ordine>> getOrdini() {
        return ResponseEntity.ok(adminService.getOrdini());
    }


    @GetMapping("/report-consumi")
    public ResponseEntity<List<Map<String, Object>>> getReportConsumi() {
        return ResponseEntity.ok(adminService.getReportConsumi());
    }

    @PostMapping("/ordini")
    public ResponseEntity<Ordine> creaOrdine(@RequestBody Ordine ordine) {
        Ordine nuovoOrdine = ordineService.creaOrdine(ordine.getFornitore(), ordine.getMateriale(), ordine.getQuantita());
        return ResponseEntity.ok(nuovoOrdine);
    }



    @GetMapping("/storico-ordini")
    public List<Ordine> getStoricoOrdini() {
        return ordineService.getStoricoOrdini();
    }

    @GetMapping("/ordini-in-attesa")
    public List<Ordine> getOrdiniInAttesa() {
        return ordineService.getOrdiniInAttesa();
    }

    @PutMapping("/ordini/{ordineId}/stato")
    public Ordine aggiornaStatoOrdine(@PathVariable Long ordineId, @RequestBody Map<String, String> payload) {
        StatoOrdine nuovoStato = StatoOrdine.valueOf(payload.get("stato"));
        return ordineService.aggiornaStatoOrdine(ordineId, nuovoStato);
    }

}

