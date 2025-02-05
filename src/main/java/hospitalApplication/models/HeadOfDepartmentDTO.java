package hospitalApplication.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeadOfDepartmentDTO {
    private Long repartoId;
    private Department department;
    private Utente capoReparto;

    public HeadOfDepartmentDTO(Long repartoId, Department department, Utente capoReparto) {
        this.repartoId = repartoId;
        this.department = department;
        this.capoReparto = capoReparto;
    }
}
