package hospitalApplication.controller;

import hospitalApplication.models.Ferie;
import hospitalApplication.models.Medicinale;
import hospitalApplication.models.Turni;
import hospitalApplication.service.AdminService;
import hospitalApplication.service.HeadOfDepartmentService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hospitalApplication.models.Department;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping("/aggiungi-medicinale")
    public ResponseEntity<String> aggiungiMedicinale(@RequestBody Medicinale medicinale) {
        String response = headOfDepartmentService.aggiungiMedicinale(medicinale);
        return ResponseEntity.ok(response);
    }


}

