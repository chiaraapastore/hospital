package hospitalApplication.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "utente")


public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username obbligatorio")
    private String username;
    @NotBlank(message = "Il nome è obbligatorio")
    private String firstName;
    @NotBlank(message = "Il cognome è obbligatorio")
    private String lastName;
    @NotBlank(message = "Email obbligatoria")
    private String email;
    private String numeroMatricola;
    private String telefono;
    @Column(name = "profile_image")
    private String profileImage;
    @Column(name = "ferie")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ferie;
    private String turno;
    private String role;
    private String password;
    private int countNotification;
    private String keycloakId;



    @ManyToOne
    @JoinColumn(name = "reparto_id", referencedColumnName = "id")
    @JsonBackReference
    @ToString.Exclude
    private Department reparto;

    public void setReparto(Department reparto) {
        this.reparto = reparto;
    }

    @Transient
    public String getRepartoNome() {
        return reparto != null ? reparto.getNome() : "Nessun reparto assegnato";
    }

}