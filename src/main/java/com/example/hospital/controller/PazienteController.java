package com.example.hospital.controller;

import com.example.hospital.models.Paziente;
import com.example.hospital.service.PazienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/pazienti")
public class PazienteController {

    private final PazienteService pazienteService;

    public PazienteController(PazienteService pazienteService) {
        this.pazienteService = pazienteService;
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Paziente> getPazienteById(@PathVariable String id) {
        Optional<Paziente> paziente = pazienteService.getPazienteById(id);
        return paziente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Paziente> savePaziente(@RequestBody Paziente paziente) {
        Paziente savedPaziente = pazienteService.savePaziente(paziente);
        return ResponseEntity.ok(savedPaziente);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePaziente(@PathVariable String id) {
        pazienteService.deletePaziente(id);
        return ResponseEntity.noContent().build();
    }
}
