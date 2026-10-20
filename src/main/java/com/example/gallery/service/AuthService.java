package com.example.gallery.service;

import com.example.gallery.models.CustomerUser;
import com.example.gallery.models.User;
import com.example.gallery.repository.UserRepository;

public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public User register(Long id, String username, String email, String password) {
        validateRegistration(id, username, email, password);

        if (userRepository.findById(id).isPresent())
            throw new IllegalStateException("User with id " + id + " already exists");
        if (userRepository.findByUsername(username).isPresent())
            throw new IllegalStateException("Username " + username + " already exists");

        User user = new CustomerUser(id, username.trim(), email.trim(), password, username.trim());
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

    private void validateRegistration(Long id, String username, String email, String password) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be a positive number");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be empty");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be empty");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password cannot be empty");
    }
}