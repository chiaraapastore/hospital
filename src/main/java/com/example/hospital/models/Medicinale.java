package com.example.hospital.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

    @Min(value = 0, message = "Il punto di riordino deve essere maggiore o uguale a 0")
    private int puntoRiordino;

    private String descrizione;

    @Positive(message = "La quantità deve essere un numero positivo")
    private int quantita;

    @Min(value = 0, message = "La quantità disponibile deve essere maggiore o uguale a 0")
    private int availableQuantity;

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
