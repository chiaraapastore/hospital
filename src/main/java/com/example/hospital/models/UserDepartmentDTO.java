package com.example.hospital.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDepartmentDTO extends Utente {
        private String repartoId;
}
