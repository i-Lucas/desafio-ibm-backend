package com.api.management.services;

import com.api.management.dtos.UserDto;
import com.api.management.models.UserModel;
import com.api.management.repositories.UserRepository;

import io.jsonwebtoken.JwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authService;

    public ResponseEntity<Object> signup(UserDto userDto) {

        if (this.checkEmailAlreadyRegistered(userDto.getEmail())) {
            return this.getResponseEntityObject(HttpStatus.CONFLICT, "E-mail already exists.");
        }

        var hashPassword = authService.hashPassword(userDto.getPassword());
        userRepository.save(new UserModel(userDto.getEmail(), hashPassword));

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    private boolean validateUserPassword(UserDto userDto, UserModel user) {
        return authService.validatePassword(userDto.getPassword(), user.getPassword());
    }

    public ResponseEntity<Object> signin(UserDto userDto) {

        UserModel user = userRepository.findByEmail(userDto.getEmail());

        if (user == null || !this.validateUserPassword(userDto, user)) {
            return this.getResponseEntityObject(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }

        var token = this.authService.generateToken(user.getEmail());
        return this.getResponseEntityObject(HttpStatus.OK, token, "token");
    }

    public ResponseEntity<Object> getUserEmailByToken(String token) {

        String jwtToken = token.substring(7); // Remove "Bearer "

        try {

            String email = authService.extractEmailFromToken(jwtToken);
            UserModel user = userRepository.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
            }

            var response = this.getHashmap();
            response.put("email", user.getEmail());
            return ResponseEntity.ok(response);

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }

    private ResponseEntity<Object> getResponseEntityObject(HttpStatus status, String message) {
        var response = this.getHashmap();
        response.put("status", status.toString());
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }

    private ResponseEntity<Object> getResponseEntityObject(HttpStatus status, String message, String type) {
        var response = this.getHashmap();
        response.put("status", status.toString());
        response.put(type, message);
        return ResponseEntity.status(status).body(response);
    }

    private Map<String, String> getHashmap() {
        return new HashMap<>();
    }

    private boolean checkEmailAlreadyRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
