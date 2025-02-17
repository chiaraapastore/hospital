package hospitalApplication.controller;

import hospitalApplication.models.Department;
import hospitalApplication.models.DoctorDTO;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/admin")
public class AdminController {


    private final AdminService adminService;
    private final DepartmentRepository departmentRepository;

    public AdminController(AdminService adminService, DepartmentRepository departmentRepository) {
        this.adminService = adminService;
        this.departmentRepository = departmentRepository;
    }

    @PostMapping("/crea-reparto")
    public ResponseEntity<String> creaReparto(@RequestBody Map<String, String> payload) {
        String nomeReparto = payload.get("nomeReparto");
        String response = adminService.creaReparto(nomeReparto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assegna-capo-reparto")
    public ResponseEntity<String> assegnaCapoReparto(@RequestParam String nomeUtente, @RequestParam Long repartoId) {
        String response = adminService.assegnaCapoReparto(nomeUtente, repartoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assegna-dottore-reparto")
    public ResponseEntity<String> assegnaDottoreAReparto(@RequestParam Long dottoreId, @RequestParam Long repartoId) {
        return ResponseEntity.ok(adminService.assegnaDottoreAReparto(dottoreId, repartoId));
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

        String repartoNome = payload.get("repartoNome");

        if (repartoNome == null || repartoNome.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Il reparto è obbligatorio.");
        }

        Department reparto = departmentRepository.findByNome(repartoNome);
        if (reparto == null) {
            return ResponseEntity.badRequest().body("Errore: Il reparto specificato non esiste.");
        }

        String response = adminService.creaDottore(
                payload.get("firstName"),
                payload.get("lastName"),
                payload.get("email"),
                reparto
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/crea-capo-reparto")
    public ResponseEntity<String> creaCapoReparto(@RequestBody Map<String, String> payload) {
        String repartoNome = payload.get("repartoNome");

        if (repartoNome == null || repartoNome.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Il reparto è obbligatorio.");
        }

        Department reparto = departmentRepository.findByNome(repartoNome);
        if (reparto == null) {
            return ResponseEntity.badRequest().body("Errore: Il reparto specificato non esiste.");
        }

        String response = adminService.creaCapoReparto(
                payload.get("firstName"),
                payload.get("lastName"),
                payload.get("email"),
                reparto
        );

        return ResponseEntity.ok(response);
    }

}

