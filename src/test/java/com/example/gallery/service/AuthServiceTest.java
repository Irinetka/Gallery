package com.example.gallery.service;

import com.example.gallery.models.User;
import com.example.gallery.models.UserRole;
import com.example.gallery.repository.InMemoryUserRepository;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты сервиса аутентификации и регистрации (AuthService).
 *
 * Проверяются:
 *  - регистрация нового пользователя (всегда с ролью CUSTOMER);
 *  - уникальность id и username;
 *  - валидация обязательных полей;
 *  - вход в систему по логину и паролю, в том числе негативные сценарии.
 */
@DisplayName("Тесты сервиса аутентификации")
class AuthServiceTest {

    private UserRepository userRepository;
    private UserService userService;
    private AuthService authService;

    /**
     * AuthService зависит от UserService и UserRepository,
     * поэтому в setUp собираемся вся цепочка зависимостей на in-memory хранилище.
     */
    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository);
        authService = new AuthService(userService, userRepository);
    }

    /**
     * Успешная регистрация посетителя.
     * Ожидание: пользователь создан, username сохранён,
     * роль автоматически установлена CUSTOMER (самостоятельная регистрация
     * администраторов и менеджеров запрещена по логике приложения).
     */
    @Test
    @DisplayName("Успешная регистрация пользователя с ролью CUSTOMER")
    void testRegisterSuccess() {
        User user = authService.register(1L, "testuser", "test@example.com", "password123");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals(UserRole.CUSTOMER, user.getRole());
    }

    /**
     * Регистрация с уже занятым id при другом username.
     * Ожидание: IllegalStateException — первичный ключ должен быть уникальным.
     */
    @Test
    @DisplayName("Регистрация с дублирующимся id")
    void testRegisterDuplicateId() {
        authService.register(1L, "user1", "user1@example.com", "password123");

        assertThrows(IllegalStateException.class, () ->
                authService.register(1L, "user2", "user2@example.com", "password123"));
    }

    /**
     * Регистрация с уже занятым username при другом id.
     * Ожидание: IllegalStateException — логин должен быть уникальным,
     * иначе аутентификация по username станет неоднозначной.
     */
    @Test
    @DisplayName("Регистрация с дублирующимся username")
    void testRegisterDuplicateUsername() {
        authService.register(1L, "testuser", "user1@example.com", "password123");

        assertThrows(IllegalStateException.class, () ->
                authService.register(2L, "testuser", "user2@example.com", "password123"));
    }

    /**
     * Регистрация с пустым паролем.
     * Ожидание: IllegalArgumentException — пароль обязателен.
     */
    @Test
    @DisplayName("Регистрация с пустым паролем")
    void testRegisterEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.register(1L, "user", "user@example.com", ""));
    }

    /**
     * Успешный вход в систему с верной парой логин/пароль.
     * Ожидание: возвращён тот же пользователь, который был зарегистрирован.
     */
    @Test
    @DisplayName("Успешный вход в систему")
    void testLoginSuccess() {
        authService.register(1L, "testuser", "test@example.com", "password123");

        User user = authService.login("testuser", "password123");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    /**
     * Вход с неверным паролем.
     * Ожидание: IllegalArgumentException("Invalid credentials").
     * Сообщение намеренно общее — не раскрывает, что именно неверно (логин или пароль).
     */
    @Test
    @DisplayName("Вход с неверным паролем")
    void testLoginWrongPassword() {
        authService.register(1L, "testuser", "test@example.com", "password123");

        assertThrows(IllegalArgumentException.class, () ->
                authService.login("testuser", "wrongpassword"));
    }

    /**
     * Вход несуществующего пользователя.
     * Ожидание: IllegalArgumentException("Invalid credentials") —
     * пустой Optional от findByUsername преобразуется в исключение.
     */
    @Test
    @DisplayName("Вход несуществующего пользователя")
    void testLoginNonExistentUser() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.login("nobody", "password123"));
    }
}