package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


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
    @Column(nullable = false)
    private boolean dimesso;
    @Temporal(TemporalType.DATE)
    private Date dataRicovero;


    @ManyToOne
    @JoinColumn(name = "reparto_id", nullable = true)
    private Department reparto;


    @ManyToOne
    @JoinColumn(name = "dottore_id", nullable = true)
    private Utente dottore;



    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Somministrazione> farmaciSomministrati;

}
