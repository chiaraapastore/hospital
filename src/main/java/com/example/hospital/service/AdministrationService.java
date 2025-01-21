package com.example.hospital.service;

import com.example.hospital.models.Administration;
import com.example.hospital.repository.AdministrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministrationService {

    private final AdministrationRepository administrationRepository;

    public Administration saveAdministration(Administration administration) {
        return administrationRepository.save(administration);
    }

    public List<Administration> getAllAdministrations() {
        return administrationRepository.findAll();
    }
}
