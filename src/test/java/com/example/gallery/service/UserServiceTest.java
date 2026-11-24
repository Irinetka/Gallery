package com.example.gallery.service;

import com.example.gallery.models.CustomerUser;
import com.example.gallery.models.User;
import com.example.gallery.models.UserRole;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для UserService.
 * Использует @SpringBootTest + Mockito (без подключения к БД).
 */
@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUserSuccess() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User created = userService.createUser(user);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("testuser", created.getUsername());
        assertEquals("test@example.com", created.getEmail());
        assertEquals("CUSTOMER", created.getRole());
    }

    @Test
    void testCreateUserDuplicateId() {
        User user1 = new CustomerUser(1L, "user1", "user1@example.com", "password123", "User 1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User user2 = new CustomerUser(1L, "user2", "user2@example.com", "password123", "User 2");

        assertThrows(IllegalStateException.class, () -> userService.createUser(user2));
    }

    @Test
    void testCreateUserNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
    }

    @Test
    void testCreateUserInvalidId() {
        User user = new CustomerUser(-1L, "user", "user@example.com", "password123", "User");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    void testCreateUserEmptyUsername() {
        User user = new CustomerUser(1L, "", "user@example.com", "password123", "User");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    void testCreateUserEmptyEmail() {
        User user = new CustomerUser(1L, "user", "", "password123", "User");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    void testGetUserSuccess() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userService.getUser(1L).orElse(null);

        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User found = userService.getUser(999L).orElse(null);

        assertNull(found);
    }

    @Test
    void testGetUserInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(-1L));
    }

    @Test
    void testShowUsers() {
        when(userRepository.findAll()).thenReturn(List.of(
                new CustomerUser(1L, "user1", "user1@example.com", "password123", "User 1"),
                new CustomerUser(2L, "user2", "user2@example.com", "password123", "User 2"),
                new CustomerUser(3L, "user3", "user3@example.com", "password123", "User 3")
        ));

        List<User> users = userService.showUsers();

        assertEquals(3, users.size());
    }

    @Test
    void testShowUsersEmpty() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> users = userService.showUsers();

        assertTrue(users.isEmpty());
    }


    @Test
    void testDeleteUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(999L));
    }

    @Test
    void testDeleteUserInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(-1L));
    }
}