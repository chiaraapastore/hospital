package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "magazine")
public class Magazine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int capienzaMassima;
    private int stockDisponibile;
    @Column(name = "punto_riordino", nullable = false)
    private int puntoRiordino = 10;


}