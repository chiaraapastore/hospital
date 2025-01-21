package com.example.hospital.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "references")
public class Reference {
    @Id
    private String id;
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;
    @NotBlank(message = "La quantità è obbligatoria")
    private int quantita;
    @NotBlank(message = "La scadenza è obbligatoria")
    private String scadenza;
    private int puntoRiordino;
}
