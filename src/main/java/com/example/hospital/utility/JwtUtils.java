package com.example.hospital.utility;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtils {

    public static String getNameFromToken(String token) {
        try{
            DecodedJWT decodedJWT = JWT.decode(token);
            String username = decodedJWT.getClaim("preferred_username").asString();
            return username;
        }catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getIdFromToken(String token) {
        try{
            DecodedJWT decodedJWT = JWT.decode(token);
            String id = decodedJWT.getClaim("sub").asString();
            return id;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }


}
