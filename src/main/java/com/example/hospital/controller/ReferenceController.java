package com.example.hospital.controller;


import com.example.hospital.models.Reference;
import com.example.hospital.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/references")
@RequiredArgsConstructor
public class ReferenceController {

    private final ReferenceService referenceService;

    @PostMapping("/create")
    public ResponseEntity<Reference> addReference(@RequestBody Reference reference) {
        return ResponseEntity.status(201).body(referenceService.addReference(reference));
    }

    @PutMapping("/{id}/update-quantity")
    public ResponseEntity<Reference> updateReferenceQuantity(
            @PathVariable String id,
            @RequestBody int quantity) {
        return ResponseEntity.ok(referenceService.updateReferenceQuantity(id, quantity));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Reference>> getAllReferences() {
        return ResponseEntity.ok(referenceService.getAllReferences());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReference(@PathVariable String id) {
        referenceService.deleteReference(id);
        return ResponseEntity.noContent().build();
    }
}

