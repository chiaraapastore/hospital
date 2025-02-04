package com.example.hospital.controller;

import com.example.hospital.models.Magazine;
import com.example.hospital.service.MagazineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/magazine")
@RequiredArgsConstructor
public class MagazineController {

    private final MagazineService magazineService;

    @GetMapping("/stock")
    public ResponseEntity<Magazine> getStock() {
        return ResponseEntity.ok(magazineService.getUserStock());
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateStock(@RequestBody Magazine magazine) {
        magazineService.updateUserStock(magazine);
        return ResponseEntity.ok().build();
    }

}
