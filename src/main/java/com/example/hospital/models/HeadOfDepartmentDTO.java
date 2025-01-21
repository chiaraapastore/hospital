package com.example.hospital.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadOfDepartmentDTO extends Utente {
    private String repartoId;
    private String[] funzioniCapoReparto = {"visualizzaScorteReparto", "aggiornaScorteReparto", "notificaRiordino"};
}