package com.example.usermanagementservice.service;

import com.example.usermanagementservice.dto.AuthRequestDTO;
import com.example.usermanagementservice.dto.UserRegistrationDTO;
import com.example.usermanagementservice.model.Role;
import com.example.usermanagementservice.model.User;
import com.example.usermanagementservice.repository.RoleRepository;
import com.example.usermanagementservice.repository.UserRepository;
import com.example.usermanagementservice.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // Register user
    public String registerUser(UserRegistrationDTO registrationDTO) {
        Optional<User> existingUser = userRepository.findByUsername(registrationDTO.getUsername());
        if (existingUser.isPresent()) {
            return "Username is already taken.";
        }

        Role role = roleRepository.findByName("USER").orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName("USER");
            return roleRepository.save(newRole);
        });

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
        return "User registered successfully.";
    }

    // Login user
    public String loginUser(AuthRequestDTO authRequestDTO) {
        // Retrieve user by username
        User user = userRepository.findByUsername(authRequestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Check if the provided password matches the stored (encoded) password
        if (!passwordEncoder.matches(authRequestDTO.getPassword(), user.getPassword())) {
            return "Invalid credentials.";
        }

        // Generate JWT token if authentication is successful
        return jwtUtils.generateJwtToken(user.getUsername());
    }

    // Example: Custom method to authenticate user credentials
    public boolean authenticate(String username, String password) {
        // For demonstration purposes, this is a mock. Replace with actual logic.
        return "test".equals(username) && "password".equals(password);
    }
}
