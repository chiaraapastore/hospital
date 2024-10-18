package com.example.hospital.controller;

import com.example.hospital.entity.TokenRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String authenticate(TokenRequest tokenRequest) {
        return "token";
    }
}
