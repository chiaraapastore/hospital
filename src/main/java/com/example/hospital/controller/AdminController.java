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

    @PostMapping("/abilita-utente/{userId}")
    public ResponseEntity<String> abilitaUtente(@PathVariable String userId) {
        String response = adminService.abilitaUtente(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/disabilita-utente/{userId}")
    public ResponseEntity<String> disabilitaUtente(@PathVariable String userId) {
        String response = adminService.disabilitaUtente(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/crea-reparto")
    public ResponseEntity<String> creaReparto(@RequestBody String nomeReparto) {
        String response = adminService.creaReparto(nomeReparto);
        return ResponseEntity.ok(response);
    }
}

