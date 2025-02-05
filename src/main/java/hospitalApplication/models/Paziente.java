package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paziente")
public class Paziente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private int eta;
    private String diagnosi;
    private String email;

    @ManyToOne
    @JoinColumn(name = "reparto_id", nullable = true)
    private Department reparto;



}
