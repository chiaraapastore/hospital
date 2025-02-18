package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ordine")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String materiale;

    @Column(nullable = false)
    private int quantita;

    @Column(nullable = false)
    private String fornitore;

    @Enumerated(EnumType.STRING)
    private StatoOrdine stato;

    private LocalDateTime dataOrdine;
}
