package hospitalApplication.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("doctor")
public class DoctorDTO extends Utente {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String matricola;
}
