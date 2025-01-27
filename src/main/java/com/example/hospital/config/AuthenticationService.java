package com.example.hospital.config;

import com.example.hospital.utility.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuthenticationService {
    private final JwtDecoder jwtDecoder;

    public AuthenticationService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public String getUsername() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Token mancante o malformato");
            }
            String token = authorizationHeader.split(" ")[1];
            return JwtUtils.getNameFromToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return "guest";
        }
    }


    public String getUserId() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String token = request.getHeader("Authorization").split(" ")[1];
            String id = JwtUtils.getIdFromToken(token);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return "No_id";
        }
    }
}
