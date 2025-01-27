package com.example.hospital.models;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "magazines")
public class Magazine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private List<Medicinale> referenze;
    private String utenteId;

    public String getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(String id) {
        this.utenteId = id;
    }
}
