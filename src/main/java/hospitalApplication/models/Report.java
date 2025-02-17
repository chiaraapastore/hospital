package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataInizio;
    private LocalDate dataFine;
    private LocalTime ora;

    @ManyToOne
    @JoinColumn(name = "paziente_id")
    private Paziente paziente;


    @ManyToOne
    @JoinColumn(name = "autore_id")
    private Utente autore;
}
