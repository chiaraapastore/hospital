package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ferie {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private LocalDate inizioFerie;
    private LocalDate fineFerie;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public Ferie(LocalDate inizioFerie, LocalDate fineFerie, Utente user) {
        this.inizioFerie  = inizioFerie;
        this.fineFerie = fineFerie;
        this.utente = user;
    }
}
