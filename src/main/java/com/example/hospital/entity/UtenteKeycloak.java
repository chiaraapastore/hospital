package com.example.hospital.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UtenteKeycloak {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
}



