package com.api.management.services;

import com.api.management.dtos.UserDto;
import com.api.management.exceptions.user.UserNotFoundException;
import com.api.management.models.UserModel;
import com.api.management.repositories.UserRepository;

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
            return this.getResponseEntityObject(HttpStatus.CONFLICT, "E-mail already exists.", "message");
        }

        var hashPassword = authService.hashPassword(userDto.getPassword());
        userRepository.save(new UserModel(userDto.getEmail(), hashPassword));

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    public ResponseEntity<Object> signin(UserDto userDto) {

        UserModel user = userRepository.findByEmail(userDto.getEmail());

        if (user == null || !this.validateUserPassword(userDto, user)) {
            return this.getResponseEntityObject(HttpStatus.UNAUTHORIZED, "Invalid email or password.", "message");
        }

        int minutes = 60;
        var token = this.authService.generateToken(user.getEmail(), minutes);
        return this.getResponseEntityObject(HttpStatus.OK, token, "token");
    }

    public ResponseEntity<Object> getUserDetails(String token) {

        var user = this.getUserByToken(token);
        var response = new HashMap<>();

        response.put("email", user.getEmail());
        response.put("id", user.getId().toString());
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Object> getResponseEntityObject(HttpStatus status, String message, String type) {
        return ResponseEntity.status(status)
                .body(Map.of("status", status.toString(), type, message));
    }

    private boolean checkEmailAlreadyRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean validateUserPassword(UserDto userDto, UserModel user) {
        return authService.validatePassword(userDto.getPassword(), user.getPassword());
    }

    private UserModel getUserByToken(String token) {

        String jwtToken = token.substring(7);
        String email = authService.extractEmailFromToken(jwtToken);

        if (email == null) {
            throw new UserNotFoundException();
        }

        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }
}
