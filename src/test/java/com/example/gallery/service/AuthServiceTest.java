package com.example.gallery.service;

import com.example.gallery.models.User;
import com.example.gallery.models.UserRole;
import com.example.gallery.repository.InMemoryUserRepository;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    private UserRepository userRepository;
    private UserService userService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository);
        authService = new AuthService(userService, userRepository);
    }

    @Test
    void testRegisterSuccess() {
        User user = authService.register(1L, "testuser", "test@example.com", "password123");
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals(UserRole.CUSTOMER, user.getRole());
    }

    @Test
    void testRegisterDuplicateId() {
        authService.register(1L, "user1", "user1@example.com", "password123");
        assertThrows(IllegalStateException.class, () ->
                authService.register(1L, "user2", "user2@example.com", "password123"));
    }

    @Test
    void testRegisterDuplicateUsername() {
        authService.register(1L, "testuser", "user1@example.com", "password123");
        assertThrows(IllegalStateException.class, () ->
                authService.register(2L, "testuser", "user2@example.com", "password123"));
    }

    @Test
    void testRegisterEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.register(1L, "user", "user@example.com", ""));
    }

    @Test
    void testLoginSuccess() {
        authService.register(1L, "testuser", "test@example.com", "password123");
        User user = authService.login("testuser", "password123");
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testLoginWrongPassword() {
        authService.register(1L, "testuser", "test@example.com", "password123");
        assertThrows(IllegalArgumentException.class, () ->
                authService.login("testuser", "wrongpassword"));
    }

    @Test
    void testLoginNonExistentUser() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.login("nobody", "password123"));
    }
}