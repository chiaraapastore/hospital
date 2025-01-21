package com.example.hospital.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdminDTO extends Utente{
    private String[] funzioniAmministrative = {"abilitaUtente", "disabilitaUtente", "creaReparto", "aggiungiReferenza", "generaReport"};
}
