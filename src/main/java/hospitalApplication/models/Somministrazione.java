package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "somministrazione")
public class Somministrazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medicinale_id", nullable = false)
    private Medicinale medicinale;

    private int quantita;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    private LocalDateTime dataOra;
}
