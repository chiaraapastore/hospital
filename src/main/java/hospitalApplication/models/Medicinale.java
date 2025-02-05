package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicinale")
public class Medicinale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;
    @NotBlank
    private String scadenza;
    @NotBlank
    private String categoria;
    @Min(0)
    private int puntoRiordino;
    private String descrizione;
    @Positive
    private int quantita;
    @Min(0)
    private int availableQuantity;
    @Min(0)
    private int scorteDisponibili;


    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "magazine_id", nullable = true)
    private Magazine magazine;

}