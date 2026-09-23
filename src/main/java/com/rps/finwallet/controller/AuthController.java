package com.rps.finwallet.controller;

import com.rps.finwallet.model.AppUser;
import com.rps.finwallet.repository.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Email is already taken!"));
        }

        // Encrypt the password using the PasswordEncoder (BCrypt)
        AppUser user = new AppUser(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        appUserRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    // DTO for Registration
    static class RegistrationRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
