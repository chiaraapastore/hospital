package com.example.hospital.service;

import com.example.hospital.models.Reference;
import com.example.hospital.repository.ReferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferenceService {

    private final ReferenceRepository referenceRepository;

    public ReferenceService(ReferenceRepository referenceRepository) {
        this.referenceRepository = referenceRepository;
    }

    public List<Reference> getAllReferences() {
        return referenceRepository.findAll();
    }

    public void deleteReference(String id) {
        referenceRepository.deleteById(id);
    }

    public Reference addReference(Reference reference) {
        return referenceRepository.save(reference);
    }

    public Reference updateReferenceQuantity(String id, int quantity) {
        Reference reference = referenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referenza non trovata"));
        reference.setQuantita(quantity);
        return referenceRepository.save(reference);
    }


}

