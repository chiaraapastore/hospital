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
public class Turni {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private LocalDate inizioTurno;
    private LocalDate fineTurno;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public Turni(LocalDate inizioTurno, LocalDate fineTurno) {
        this.inizioTurno = inizioTurno;
        this.fineTurno = fineTurno;
    }

    public Turni(LocalDate inizioTurno, LocalDate fineTurno, Utente user) {
        this.inizioTurno  = inizioTurno;
        this.fineTurno = fineTurno;
        this.utente = user;
    }
}
