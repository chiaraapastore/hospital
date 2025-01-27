package com.example.hospital.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeadOfDepartmentDTO extends Utente {
    private String repartoId;
    private HeadOfDepartmentId headOfDepartmentId;
    private Department department;
    private Utente capoReparto;

    public HeadOfDepartmentDTO(HeadOfDepartmentId headOfDepartmentId, Department department, Utente capoReparto) {
        this.headOfDepartmentId = headOfDepartmentId;
        this.department = department;
        this.capoReparto = capoReparto;
    }
}