package hospitalApplication.models;

import lombok.*;
import hospitalApplication.models.Utente;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UtenteDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String numeroMatricola;
    private String telefono;
    private String profileImage;
    private String role;
    private int countNotification;
    private String keycloakId;

    private Long repartoId;
    private String nomeReparto;

    public UtenteDTO(Utente utente) {
        this.id = utente.getId();
        this.username = utente.getUsername();
        this.firstName = utente.getFirstName();
        this.lastName = utente.getLastName();
        this.email = utente.getEmail();
        this.numeroMatricola = utente.getNumeroMatricola();
        this.telefono = utente.getTelefono();
        this.profileImage = utente.getProfileImage();
        this.role = utente.getRole();
        this.countNotification = utente.getCountNotification();
        this.keycloakId = utente.getKeycloakId();
        if (utente.getReparto() != null) {
            this.repartoId = utente.getReparto().getId();
            this.nomeReparto = utente.getReparto().getNome();
        } else {
            this.repartoId = null;
            this.nomeReparto = "Nessun reparto assegnato";
        }

    }

}
