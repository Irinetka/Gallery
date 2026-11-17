package com.example.gallery.controller;

import com.example.gallery.models.User;
import com.example.gallery.models.UserRole;
import com.example.gallery.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request.id(), request.username(), request.email(),
                request.password(), request.role(), request.fullName(), request.specialization());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        User user = authService.login(request.username(), request.password());
        return ResponseEntity.ok(user);
    }

    public record RegisterRequest(Long id, String username, String email, String password,
                                  UserRole role, String fullName, String specialization) {}
    public record LoginRequest(String username, String password) {}
}