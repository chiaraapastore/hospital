package com.example.hospital.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.RoleRepresentation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleKeycloak {
    private String id;
    private String role;
    private String client;
    private String client_id;
    private String description_role;

    public RoleRepresentation toRoleRepresentation(){
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setId(this.id);
        roleRepresentation.setName(this.role);
        roleRepresentation.setDescription(this.description_role);
        return roleRepresentation;
    }
}
