package com.example.hospital.controller;

import com.example.hospital.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utente-reparto")
public class DoctorController {

    private final DoctorService userDepartmentService;

    public DoctorController(DoctorService userDepartmentService) {
        this.userDepartmentService = userDepartmentService;
    }

    @GetMapping("/visualizza-referenze/{repartoId}")
    public ResponseEntity<String> visualizzaReferenzeReparto(@PathVariable String repartoId) {
        String response = userDepartmentService.visualizzaReferenzeReparto(repartoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/somministra-medicine")
    public ResponseEntity<String> somministraMedicine(@RequestParam String pazienteId, @RequestParam String medicinaleId) {
        String response = userDepartmentService.somministraMedicine(pazienteId, medicinaleId);
        return ResponseEntity.ok(response);
    }
}
