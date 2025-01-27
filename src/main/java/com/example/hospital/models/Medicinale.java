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
@Document(collection = "medicines")
public class Medicinale {
    @Id
    private String id;
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;
    @NotBlank(message = "La scadenza è obbligatoria")
    private String scadenza;
    @NotBlank(message = "La categoria è obbligatoria")
    private String categoria;
    private int puntoRiordino;
    private String descrizione;
    @NotBlank(message = "La quantità è obbligatoria")
    private int quantita;
    private int getAvailableQuantity;

    public int getAvailableQuantity() {
        return getAvailableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        getAvailableQuantity = availableQuantity;
    }
}
