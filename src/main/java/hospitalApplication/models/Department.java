package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.List;

import java.util.ArrayList;

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

    @OneToOne
    @JoinColumn(name = "capo_reparto_id", referencedColumnName = "id", unique = true)
    private Utente capoReparto;

}