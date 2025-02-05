package hospitalApplication.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("DOCTOR")
public class DoctorDTO extends Utente {
        private String specialty;
}
