package com.api.management.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Service;

import com.api.management.exceptions.InvalidTokenException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthenticationService {

    private final String SECRET_KEY = "your-secret-key";

    public String generateToken(String email) {

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 3600000); // 1h

        return Jwts.builder().setSubject(email).setIssuedAt(now).setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, this.SECRET_KEY).compact();
    }

    public String extractEmailFromToken(String jwtToken) {

        try {

            Jws<Claims> claims = Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(jwtToken);
            return claims.getBody().getSubject();

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