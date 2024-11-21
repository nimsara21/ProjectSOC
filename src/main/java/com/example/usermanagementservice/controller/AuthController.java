package com.example.usermanagementservice.controller;

import com.example.usermanagementservice.dto.UserRegistrationDTO;
import com.example.usermanagementservice.service.UserService;
import com.example.usermanagementservice.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // User registration endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        String result = userService.registerUser(registrationDTO);
        return ResponseEntity.ok(result);
    }

    // Login endpoint that generates JWT token
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        if (userService.authenticate(username, password)) {
            String jwt = JwtUtils.generateJwtToken(username);
            return ResponseEntity.ok(jwt);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
