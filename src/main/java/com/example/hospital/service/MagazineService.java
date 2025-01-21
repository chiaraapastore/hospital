package com.example.hospital.service;


import com.example.hospital.models.Magazine;
import com.example.hospital.repository.MagazineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class MagazineService {
    private final MagazineRepository magazineRepository;

    public Magazine getStock() {
        return magazineRepository.findAll().stream().findFirst().orElse(null);
    }

    public void updateStock(Magazine magazine) {
        magazineRepository.save(magazine);
    }
}