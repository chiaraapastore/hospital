package com.example.hospital.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {
    private String username;
    private String password;
    private String client_id;
    private String client_secret;
    private String grant_type;
}

