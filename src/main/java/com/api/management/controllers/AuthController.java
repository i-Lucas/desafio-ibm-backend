package com.api.management.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.management.dtos.UserDto;
import com.api.management.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserDto userDto) {
        return this.userService.signup(userDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid UserDto userDto) {
        return this.userService.signin(userDto);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token) {
        return userService.getUserEmailByToken(token);
    }

}