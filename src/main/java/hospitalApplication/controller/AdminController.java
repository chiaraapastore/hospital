package hospitalApplication.controller;

import hospitalApplication.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/admin")
public class AdminController {


    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
}

