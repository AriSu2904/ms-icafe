package com.atm.inet.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtSecurityConfig {

    @Value("${inet.jwt-secret}")
    private String jwtSecret;

    @Value("${inet.jwt-expiration}")
    private Long jwtExpired;

    public String getEmailByToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpired))
                .compact();
    }

    public Boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException e){
            log.error(e.getMessage());
        }
        return false;
    }

}
