package com.example.hospital.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "notifications")
public class Notification {
    @Id
    private String id;
    private String messaggio;
    private boolean letta;
    private Utente userId;
    private LocalDateTime dataOra;
}
