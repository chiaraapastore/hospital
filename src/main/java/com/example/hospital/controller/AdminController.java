package com.example.hospital.controller;

import com.example.hospital.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {


    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/crea-reparto")
    public ResponseEntity<String> creaReparto(@RequestBody String nomeReparto) {
        String response = adminService.creaReparto(nomeReparto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/aggiungi-utente-reparto")
    public String aggiungiUtenteAReparto(@RequestParam String utenteId, @RequestParam String repartoId) {
        return adminService.aggiungiUtenteAReparto(utenteId, repartoId);
    }

    @PostMapping("/assegna-capo-reparto")
    public String assegnaCapoReparto(@RequestParam String utenteId, @RequestParam String repartoId) {
        return adminService.assegnaCapoReparto(utenteId, repartoId);
    }
}

