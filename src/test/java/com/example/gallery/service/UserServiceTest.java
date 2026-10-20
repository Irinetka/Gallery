package com.example.gallery.service;

import com.example.gallery.models.CustomerUser;
import com.example.gallery.models.User;
import com.example.gallery.models.UserRole;
import com.example.gallery.repository.InMemoryUserRepository;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository);
    }

    @Test
    void testCreateUserSuccess() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");
        User created = userService.createUser(user);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("testuser", created.getUsername());
        assertEquals("test@example.com", created.getEmail());
        assertEquals(UserRole.CUSTOMER, created.getRole());
    }

    @Test
    void testCreateUserDuplicateId() {
        User user1 = new CustomerUser(1L, "user1", "user1@example.com", "password123", "User 1");
        userService.createUser(user1);

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
        userService.createUser(user);

        User found = userService.getUser(1L).orElse(null);

        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
    }

    @Test
    void testGetUserNotFound() {
        User found = userService.getUser(999L).orElse(null);

        assertNull(found);
    }

    @Test
    void testGetUserInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(-1L));
    }

    @Test
    void testShowUsers() {
        userService.createUser(new CustomerUser(1L, "user1", "user1@example.com", "password123", "User 1"));
        userService.createUser(new CustomerUser(2L, "user2", "user2@example.com", "password123", "User 2"));
        userService.createUser(new CustomerUser(3L, "user3", "user3@example.com", "password123", "User 3"));

        List<User> users = userService.showUsers();

        assertEquals(3, users.size());
    }

    @Test
    void testShowUsersEmpty() {
        List<User> users = userService.showUsers();

        assertTrue(users.isEmpty());
    }

    @Test
    void testDeleteUserSuccess() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");
        userService.createUser(user);

        userService.deleteUser(1L);

        assertTrue(userService.getUser(1L).isEmpty());
    }

    @Test
    void testDeleteUserNotFound() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(999L));
    }

    @Test
    void testDeleteUserInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(-1L));
    }
}