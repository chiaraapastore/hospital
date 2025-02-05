package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
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

    private String role;
    private String password;
    private int countNotification;
    private String keycloakId;


    @ManyToOne
    @JoinColumn(name = "reparto_id")
    private Department reparto;

    public void setReparto(Department reparto) {
        this.reparto = reparto;
    }
}