package com.example.hospital.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "departments")
public class Department {
    @Id
    private String id;
    private String nome;
    private Utente capoReparto;
    private List<Medicinale> scorte;
    private List<Utente> utenti;

}
