package hospitalApplication.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nome;

    private int countMedicinali;


    @OneToOne
    @JoinColumn(name = "capo_reparto_id", referencedColumnName = "id", unique = true)
    @JsonManagedReference
    @ToString.Exclude
    private Utente capoReparto;

}