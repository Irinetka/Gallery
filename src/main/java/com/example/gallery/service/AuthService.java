package com.example.gallery.service;

import com.example.gallery.exception.DuplicateEntityException;
import com.example.gallery.exception.InvalidCredentialsException;
import com.example.gallery.exception.ValidationException;
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
            throw new DuplicateEntityException("User with id " + id + " already exists",
                    "Choose another ID");
        if (userRepository.findByUsername(username).isPresent())
            throw new DuplicateEntityException("Username " + username + " already exists",
                    "Choose another username");
        User user = switch (role) {
            case ADMIN -> new AdminUser(id, username.trim(), email.trim(), password);
            case MANAGER -> new ManagerUser(id, username.trim(), email.trim(), password, fullName, specialization);
            default -> new CustomerUser(id, username.trim(), email.trim(), password, fullName);
        };
        return userService.createUser(user);
    }

    public User login(String username, String password) {
        if (username == null || username.isBlank())
            throw new ValidationException("Username cannot be empty", "Enter your login");
        if (password == null || password.isBlank())
            throw new ValidationException("Password cannot be empty", "Enter your password");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials",
                        "No such user; check the username or register first"));
        if (!user.getPassword().equals(password))
            throw new InvalidCredentialsException("Invalid credentials",
                    "Wrong password; try again or restore access");
        return user;
    }

    private void validateRegistration(Long id, String username, String email,
                                      String password, UserRole role) {
        if (id == null || id <= 0) throw new ValidationException("Id must be positive",
                "ID must be a positive number");
        if (username == null || username.isBlank()) throw new ValidationException("Username cannot be empty",
                "Fill the 'username' field");
        if (email == null || email.isBlank()) throw new ValidationException("Email cannot be empty",
                "Fill the 'email' field");
        if (password == null || password.isBlank()) throw new ValidationException("Password cannot be empty",
                "Fill the 'password' field");
        if (role == null) throw new ValidationException("User role cannot be null",
                "Set 'role' to ADMIN, MANAGER or CUSTOMER");
    }
}