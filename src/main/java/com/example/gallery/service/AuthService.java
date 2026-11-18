package com.example.gallery.service;

import com.example.gallery.models.*;
import com.example.gallery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public User register(Long id, String username, String email, String password,
                         UserRole role, String fullName, String specialization) {
        validateRegistration(id, username, email, password, role);
        if (userRepository.findById(id).isPresent())
            throw new IllegalStateException("User with id " + id + " already exists");
        if (userRepository.findByUsername(username).isPresent())
            throw new IllegalStateException("Username " + username + " already exists");

        User user = switch (role) {
            case ADMIN -> new AdminUser(id, username.trim(), email.trim(), password);
            case MANAGER -> new ManagerUser(id, username.trim(), email.trim(), password, fullName, specialization);
            default -> new CustomerUser(id, username.trim(), email.trim(), password, fullName);
        };
        return userService.createUser(user);
    }

    public User login(String username, String password) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be empty");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!user.getPassword().equals(password))
            throw new IllegalArgumentException("Invalid credentials");
        return user;
    }

    private void validateRegistration(Long id, String username, String email,
                                      String password, UserRole role) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be positive");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be empty");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be empty");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password cannot be empty");
        if (role == null) throw new IllegalArgumentException("User role cannot be null");
    }
}