package hospitalApplication.controller;

import hospitalApplication.service.AdminService;
import hospitalApplication.service.HeadOfDepartmentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capo-reparto")
public class HeadOfDepartmentController {

    private final HeadOfDepartmentService headOfDepartmentService;
    private final AdminService adminService;

    public HeadOfDepartmentController(HeadOfDepartmentService headOfDepartmentService, AdminService adminService) {
        this.headOfDepartmentService = headOfDepartmentService;
        this.adminService = adminService;
    }

    @PutMapping("/aggiorna-scorte/{repartoId}/{medicinaId}")
    public ResponseEntity<String> aggiornaScorteReparto(
            @PathVariable Long repartoId,
            @PathVariable Long medicinaId,
            @RequestParam int nuovaQuantita
    ) {
        String response = headOfDepartmentService.aggiornaScorteReparto(repartoId, medicinaId, nuovaQuantita);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/assegna-dottore-reparto")
    public ResponseEntity<String> assegnaDottoreAReparto(@RequestParam Long dottoreId, @RequestParam Long repartoId) {
        return ResponseEntity.ok(adminService.assegnaDottoreAReparto(dottoreId, repartoId));
    }

    @PostMapping("/notifica/{repartoId}")
    public ResponseEntity<String> inviaNotifica(@PathVariable String repartoId, @RequestBody String messaggio) {
        String response = headOfDepartmentService.inviaNotifica(repartoId, messaggio);
        return ResponseEntity.ok(response);
    }
}

