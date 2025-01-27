package com.example.hospital.controller;

import com.example.hospital.service.HeadOfDepartmentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utente-reparto")
public class HeadOfDepartmentController {

    private final HeadOfDepartmentService headOfDepartmentService;

    public HeadOfDepartmentController(HeadOfDepartmentService headOfDepartmentService) {
        this.headOfDepartmentService = headOfDepartmentService;
    }

    @PutMapping("/aggiorna-scorte/{repartoId}/{referenzaId}")
    public ResponseEntity<String> aggiornaScorteReparto(
            @PathVariable String repartoId,
            @PathVariable String referenzaId,
            @RequestParam int nuovaQuantita
    ) {
        String response = headOfDepartmentService.aggiornaScorteReparto(repartoId, referenzaId, nuovaQuantita);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notifica/{repartoId}")
    public ResponseEntity<String> inviaNotifica(@PathVariable String repartoId, @RequestBody String messaggio) {
        String response = headOfDepartmentService.inviaNotifica(repartoId, messaggio);
        return ResponseEntity.ok(response);
    }
}

