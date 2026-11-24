package com.example.gallery.service;

import com.example.gallery.exception.GalleryException;
import com.example.gallery.models.CustomerUser;
import com.example.gallery.models.User;
import com.example.gallery.models.UserRole;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для AuthService.
 * Использует @SpringBootTest + Mockito (без подключения к БД).
 */
@SpringBootTest
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegisterSuccess() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.empty());
        when(userService.createUser(any())).thenReturn(user);

        User created = authService.register(1L, "testuser", "test@example.com", "password123",
                UserRole.CUSTOMER, "Test User", null);

        assertNotNull(created);
        assertEquals("testuser", created.getUsername());
        assertEquals("CUSTOMER", created.getRole());
    }

    @Test
    void testRegisterDuplicateId() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(new CustomerUser()));

        assertThrows(GalleryException.class, () ->
                authService.register(1L, "user2", "user2@example.com", "password123",
                        UserRole.CUSTOMER, null, null));
    }

    @Test
    void testRegisterDuplicateUsername() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(new CustomerUser()));

        assertThrows(GalleryException.class, () ->
                authService.register(2L, "testuser", "user2@example.com", "password123",
                        UserRole.CUSTOMER, null, null));
    }

    @Test
    void testRegisterEmptyPassword() {
        assertThrows(GalleryException.class, () ->
                authService.register(1L, "user", "user@example.com", "",
                        UserRole.CUSTOMER, null, null));
    }

    @Test
    void testLoginSuccess() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));

        User loggedIn = authService.login("testuser", "password123");

        assertNotNull(loggedIn);
        assertEquals("testuser", loggedIn.getUsername());
    }

    @Test
    void testLoginWrongPassword() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));

        assertThrows(GalleryException.class, () ->
                authService.login("testuser", "wrongpassword"));
    }

    @Test
    void testLoginNonExistentUser() {
        when(userRepository.findByUsername("nobody")).thenReturn(java.util.Optional.empty());

        assertThrows(GalleryException.class, () ->
                authService.login("nobody", "password123"));
    }
}