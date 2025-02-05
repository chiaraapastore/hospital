package hospitalApplication.controller;

import hospitalApplication.service.AdminService;
import hospitalApplication.service.HeadOfDepartmentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utente-reparto")
public class HeadOfDepartmentController {

    private final HeadOfDepartmentService headOfDepartmentService;
    private final AdminService adminService;

    public HeadOfDepartmentController(HeadOfDepartmentService headOfDepartmentService, AdminService adminService) {
        this.headOfDepartmentService = headOfDepartmentService;
        this.adminService = adminService;
    }

    @PutMapping("/aggiorna-scorte/{repartoId}/{referenzaId}")
    public ResponseEntity<String> aggiornaScorteReparto(
            @PathVariable Long repartoId,
            @PathVariable Long referenzaId,
            @RequestParam int nuovaQuantita
    ) {
        String response = headOfDepartmentService.aggiornaScorteReparto(repartoId, referenzaId, nuovaQuantita);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/assegna-paziente-reparto")
    public ResponseEntity<String> assegnaPazienteAReparto(@RequestParam Long pazienteId, @RequestParam Long repartoId) {
        return ResponseEntity.ok(adminService.assegnaPazienteAReparto(pazienteId, repartoId));
    }

    @PostMapping("/notifica/{repartoId}")
    public ResponseEntity<String> inviaNotifica(@PathVariable String repartoId, @RequestBody String messaggio) {
        String response = headOfDepartmentService.inviaNotifica(repartoId, messaggio);
        return ResponseEntity.ok(response);
    }
}

