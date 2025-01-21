package com.example.hospital.controller;

import com.example.hospital.models.Administration;
import com.example.hospital.service.AdministrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrations")
@RequiredArgsConstructor
public class AdministrationController {

    private final AdministrationService administrationService;

    @PostMapping("/register")
    public ResponseEntity<Administration> registerAdministration(@RequestBody Administration administration) {
        return ResponseEntity.ok(administrationService.saveAdministration(administration));
    }

    @GetMapping("/listaAmministrazioni")
    public ResponseEntity<List<Administration>> getAllAdministrations() {
        return ResponseEntity.ok(administrationService.getAllAdministrations());
    }
}
