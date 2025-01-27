package com.example.hospital.controller;

import com.example.hospital.models.Medicinale;
import com.example.hospital.service.MedicinaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/medicinali")
public class MedicinaleController {

    private final MedicinaleService medicinaleService;

    public MedicinaleController(MedicinaleService medicinaleService) {
        this.medicinaleService = medicinaleService;
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Medicinale> getMedicinaleById(@PathVariable String id) {
        Optional<Medicinale> medicinale = medicinaleService.getMedicinaleById(id);
        return medicinale.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Medicinale> saveMedicinale(@RequestBody Medicinale medicinale) {
        Medicinale savedMedicinale = medicinaleService.saveMedicinale(medicinale);
        return ResponseEntity.ok(savedMedicinale);
    }

    @PutMapping("/{id}/update-quantity")
    public ResponseEntity<Medicinale> updateMedicinaleQuantity(
            @PathVariable String id,
            @RequestBody int quantity) {
        return ResponseEntity.ok(medicinaleService.updateMedicinaleQuantity(id, quantity));
    }

    @DeleteMapping("/delete/{id}/")
    public ResponseEntity<Void> deleteMedicinale(@PathVariable String id) {
        medicinaleService.deleteMedicinale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/check-reorder")
    public ResponseEntity<String> checkReorderPoint(@PathVariable String id) {
        Optional<Medicinale> medicinaleOpt = medicinaleService.getMedicinaleById(id);

        if (medicinaleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Medicinale medicinale = medicinaleOpt.get();
        int reorderPoint = medicinale.getPuntoRiordino();
        int currentQuantity = medicinale.getQuantita();

        if (currentQuantity <= reorderPoint) {
            return ResponseEntity.ok("È necessario riordinare il medicinale: " + medicinale.getNome());
        }

        return ResponseEntity.ok("Il medicinale: " + medicinale.getNome() + " ha una quantità sufficiente.");
    }



}