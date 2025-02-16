package hospitalApplication.controller;

import hospitalApplication.service.AdminService;
import hospitalApplication.service.HeadOfDepartmentService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hospitalApplication.models.Department;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
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

    @PutMapping("/assegna-ferie/{doctorId}")
    public ResponseEntity<Void> assegnaFerie(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFerie) {
        headOfDepartmentService.assegnaFerie(doctorId, dataFerie);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/notifica/{repartoId}")
    public ResponseEntity<String> inviaNotifica(@PathVariable String repartoId, @RequestBody String messaggio) {
        String response = headOfDepartmentService.inviaNotifica(repartoId, messaggio);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/reparti")
    public ResponseEntity<List<Department>> getReparti(){
        List<Department> reparti = headOfDepartmentService.getReparti();
        return ResponseEntity.ok(reparti);
    }

    @GetMapping("/ferie-disponibili")
    public ResponseEntity<List<String>> getFerieDisponibili(){
        List<String> ferie = headOfDepartmentService.getFerieDisponibili();
        return ResponseEntity.ok(ferie);
    }



}

