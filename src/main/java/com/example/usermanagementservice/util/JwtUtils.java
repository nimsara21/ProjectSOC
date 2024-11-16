package com.example.usermanagementservice.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private final String jwtSecret = "NimzuprivateKey";
    private final long jwtExpirationMs = 86400000;

    public String generateJwtToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }





}
