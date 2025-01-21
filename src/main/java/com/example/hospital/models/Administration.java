package com.example.hospital.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "administrations")
public class Administration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String referenceId;
    private int quantita;
    private LocalDateTime dataOra;
    private Utente utenteId;
}
