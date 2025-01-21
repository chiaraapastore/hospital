package com.example.hospital.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @NotBlank(message = "Il tipo è obbligatorio")
    private String tipo;
    @NotBlank(message = "I dati sono obbligatori")
    private String dati;
    private String inizio;
    private String fine;

    public Comparable<String> getInizio() {
        return inizio;
    }

    public Comparable<String> getFine() {
        return fine;
    }


}
