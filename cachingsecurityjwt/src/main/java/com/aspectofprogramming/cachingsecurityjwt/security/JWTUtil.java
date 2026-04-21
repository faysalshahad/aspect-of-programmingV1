package com.aspectofprogramming.cachingsecurityjwt.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

    @Value("${app.jwt.secret}")
    private String jwtSecretKey;

    @Value("${app.expiration.access-token-exp}")
    private long accessTokenExpTime;

    private Date todayDate = new Date();

    private Date expiredDateTime = new Date(System.currentTimeMillis() + accessTokenExpTime);

    private Key getSigningKey(){
        byte [] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
        }

    public String generateAccessToken(String username){
        return Jwts
        .builder()
        .subject(username)
        // .claim(role_, this)
        .issuedAt(todayDate)
        .expiration(expiredDateTime)
        .signWith(getSigningKey())
        .compact();
    }

    public String generateRefreshToken(){
        return java.util.UUID.randomUUID().toString();
    }

    public String getUsernameFromToken(String token){
        return Jwts
        .parser()
        .verifyWith((SecretKey)getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts
            .parser()
            .verifyWith((SecretKey)getSigningKey())
            .build()
            .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Jwt Exception Error");
            return false;
        }
    }

  
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey)getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
