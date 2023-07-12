package com.api.management.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Service;

import com.api.management.exceptions.token.InvalidTokenException;
import com.api.management.exceptions.token.TokenExpiredException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthenticationService {

    private final String SECRET_KEY = "I_LOVE_JAVA";

    public String generateToken(String email, int minutes) {

        Date now = new Date();
        long expirationMillis = minutes * 60000;
        Date expirationDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, this.SECRET_KEY)
                .compact();
    }

    public String extractEmailFromToken(String jwtToken) {

        try {

            Jws<Claims> claims = Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(jwtToken);
            return claims.getBody().getSubject();

        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();

        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    public String hashPassword(String password) {

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password.", e);
        }
    }

    public boolean validatePassword(String inputPassword, String storedPassword) {
        String hashedInputPassword = this.hashPassword(inputPassword);
        return hashedInputPassword.equals(storedPassword);
    }
}