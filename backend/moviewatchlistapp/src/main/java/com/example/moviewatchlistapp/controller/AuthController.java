package com.example.moviewatchlistapp.controller;

import com.example.moviewatchlistapp.model.User;
import com.example.moviewatchlistapp.repository.UserRepository;
import com.example.moviewatchlistapp.authorization.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            return ResponseEntity.badRequest().body("USERNAME_EMPTY");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("PASSWORD_EMPTY");
        }

        Optional<User> existing = userRepository.findByUsername(user.getUsername());

        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("USERNAME_EXISTS");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("REGISTERED");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {

        Optional<User> dbUser = userRepository.findByUsername(user.getUsername());

        if (dbUser.isPresent() &&
            encoder.matches(user.getPassword(), dbUser.get().getPassword())) {

            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("INVALID");
    }
}