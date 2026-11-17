package com.example.gallery.service;

import com.example.gallery.models.CustomerUser;
import com.example.gallery.models.User;
import com.example.gallery.models.UserRole;
import com.example.gallery.repository.InMemoryUserRepository;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты базового сервиса управления пользователями (UserService).

 * Проверяется валидация обязательных полей (id, username, email, role),
 * запрет дубликатов id и корректность CRUD-операций.
 * В качестве конкретной реализации абстрактного класса User
 * используется наследник CustomerUser — это одновременно проверяет
 * работоспособность иерархии наследования.
 */
@DisplayName("Тесты сервиса пользователей")
class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    /** Новое пустое хранилище пользователей перед каждым тестом. */
    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository);
    }

    /**
     * Успешное создание пользователя типа CustomerUser.
     * Ожидание: все поля сохранены, роль CUSTOMER установлена
     * конструктором наследника (полиморфизм: работаем через ссылку типа User).
     */
    @Test
    @DisplayName("Создание пользователя с корректными данными")
    void testCreateUserSuccess() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");

        User created = userService.createUser(user);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("testuser", created.getUsername());
        assertEquals("test@example.com", created.getEmail());
        assertEquals(UserRole.CUSTOMER, created.getRole());
    }

    /**
     * Создание пользователя с уже занятым id.
     * Ожидание: IllegalStateException.
     */
    @Test
    @DisplayName("Создание пользователя с дублирующимся id")
    void testCreateUserDuplicateId() {
        User user1 = new CustomerUser(1L, "user1", "user1@example.com", "password123", "User 1");
        userService.createUser(user1);

        User user2 = new CustomerUser(1L, "user2", "user2@example.com", "password123", "User 2");

        assertThrows(IllegalStateException.class, () -> userService.createUser(user2));
    }

    /**
     * Передача null вместо пользователя.
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Создание пользователя из null-объекта")
    void testCreateUserNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
    }

    /**
     * Отрицательный id пользователя.
     * Ожидание: IllegalArgumentException — id должен быть положительным.
     */
    @Test
    @DisplayName("Создание пользователя с невалидным id")
    void testCreateUserInvalidId() {
        User user = new CustomerUser(-1L, "user", "user@example.com", "password123", "User");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    /**
     * Пустой username.
     * Ожидание: IllegalArgumentException — логин обязателен для аутентификации.
     */
    @Test
    @DisplayName("Создание пользователя с пустым username")
    void testCreateUserEmptyUsername() {
        User user = new CustomerUser(1L, "", "user@example.com", "password123", "User");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    /**
     * Пустой email.
     * Ожидание: IllegalArgumentException — email обязателен по правилам валидации сервиса.
     */
    @Test
    @DisplayName("Создание пользователя с пустым email")
    void testCreateUserEmptyEmail() {
        User user = new CustomerUser(1L, "user", "", "password123", "User");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    /**
     * Успешное получение пользователя по id.
     * Ожидание: Optional содержит пользователя с нужным username.
     */
    @Test
    @DisplayName("Получение существующего пользователя")
    void testGetUserSuccess() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");
        userService.createUser(user);

        User found = userService.getUser(1L).orElse(null);

        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
    }

    /**
     * Получение несуществующего пользователя.
     * Ожидание: пустой Optional (без исключения).
     */
    @Test
    @DisplayName("Получение несуществующего пользователя")
    void testGetUserNotFound() {
        User found = userService.getUser(999L).orElse(null);

        assertNull(found);
    }

    /**
     * Получение пользователя с невалидным id.
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Получение пользователя с невалидным id")
    void testGetUserInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(-1L));
    }

    /**
     * Получение списка всех пользователей.
     * Ожидание: 3 записи — по числу созданных пользователей.
     */
    @Test
    @DisplayName("Получение списка всех пользователей")
    void testShowUsers() {
        userService.createUser(new CustomerUser(1L, "user1", "user1@example.com", "password123", "User 1"));
        userService.createUser(new CustomerUser(2L, "user2", "user2@example.com", "password123", "User 2"));
        userService.createUser(new CustomerUser(3L, "user3", "user3@example.com", "password123", "User 3"));

        List<User> users = userService.showUsers();

        assertEquals(3, users.size());
    }

    /**
     * Список пользователей пустого хранилища.
     * Ожидание: пустой список, а не null.
     */
    @Test
    @DisplayName("Получение пустого списка пользователей")
    void testShowUsersEmpty() {
        List<User> users = userService.showUsers();

        assertTrue(users.isEmpty());
    }

    /**
     * Успешное удаление пользователя.
     * Ожидание: после удаления пользователь не находится по id.
     */
    @Test
    @DisplayName("Удаление существующего пользователя")
    void testDeleteUserSuccess() {
        User user = new CustomerUser(1L, "testuser", "test@example.com", "password123", "Test User");
        userService.createUser(user);

        userService.deleteUser(1L);

        assertTrue(userService.getUser(1L).isEmpty());
    }

    /**
     * Удаление несуществующего пользователя.
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Удаление несуществующего пользователя")
    void testDeleteUserNotFound() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(999L));
    }

    /**
     * Удаление пользователя с невалидным id.
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Удаление пользователя с невалидным id")
    void testDeleteUserInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(-1L));
    }
}