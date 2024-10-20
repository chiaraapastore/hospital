package com.example.hospital.entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.hospital.repository.UtenteRepository;

@Setter
@Getter
@Document(value ="utenti")
@AllArgsConstructor
@NoArgsConstructor

public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;
    private String email;
    private String password;
    private UtenteRepository utenteRepository;


    public boolean isPresent() {
        if(utenteRepository.findByEmail(email).isPresent()) {
            return true;
        }else{
            return false;
        }
    }
}
