package com.example.hospital.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "patients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paziente {
    @Id
    private String id;
    private String nome;
    private String cognome;
    private int eta;
    private String diagnosi;
    private String email;
}
