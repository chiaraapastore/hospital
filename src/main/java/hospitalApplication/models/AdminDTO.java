package hospitalApplication.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("ADMIN")
public class AdminDTO extends Utente {
    private String adminLevel;
}
