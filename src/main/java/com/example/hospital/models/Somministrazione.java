package com.example.hospital.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Document(value = "somministrazione")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Somministrazione {
    @Id
    private String id;
    private Medicinale medicinaleId;
    private int quantita;
    private Utente utenteId;
    private LocalDateTime dataOra;


}
