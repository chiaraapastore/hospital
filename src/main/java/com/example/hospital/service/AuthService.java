package com.example.hospital.service;

import com.example.hospital.entity.TokenRequest;
import com.example.hospital.utility.JwtUtils;
import com.example.hospital.entity.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    // Metodo per autenticare l'utente e generare un token JWT
    public String authenticate(TokenRequest tokenRequest) {
        //controllo credenziali
        if ("user@example.com".equals(tokenRequest.getUsername()) && "password".equals(tokenRequest.getPassword())) {
            Utente utente = new Utente();
            utente.setFirstName("Username"); // Imposta il nome utente
            utente.setEmail(tokenRequest.getUsername()); // Imposta l'email
            return jwtUtils.generateToken(utente); // Restituisce il token JWT
        } else {
            throw new RuntimeException("Credenziali non valide");
        }
    }
}
