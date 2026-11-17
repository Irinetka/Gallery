package com.example.gallery.console;

import com.example.gallery.models.UserRole;
import com.example.gallery.models.User;
import com.example.gallery.service.AuthService;

// Класс AuthMenu — меню аутентификации, реализующее вход в систему и регистрацию нового пользователя
public class AuthMenu {

    // Поле authService — сервис аутентификации и регистрации
    private final AuthService authService;
    // Поле session — сессия текущего пользователя
    private final Session session;
    // Поле console — утилита для безопасного чтения пользовательского ввода
    private final ConsoleUtils console;

    // Конструктор с параметрами — инициализируются все зависимости
    public AuthMenu(AuthService authService, Session session, ConsoleUtils console) {
        this.authService = authService;
        this.session = session;
        this.console = console;
    }

    // Метод show — отображает стартовое меню авторизации в цикле, пока пользователь не вошёл в систему
    public void show() {
        // Цикл работает, пока пользователь не аутентифицирован
        while (!session.isAuthenticated()) {
            // Выводится заголовок меню
            System.out.println();
            System.out.println("GALLERY APPLICATION");
            System.out.println();
            // Выводятся пункты меню
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            System.out.println();

            // Считывается выбор пользователя через console
            String choice = console.readString("Select action: ");

            try {
                // Обрабатывается выбор с помощью switch
                switch (choice) {
                    case "1" -> login();       // вызывается метод login — вход в систему
                    case "2" -> register();    // вызывается метод register — регистрация нового пользователя
                    case "0" -> {              // выход из приложения
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Unknown command."); // неизвестная команда
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                // Обрабатываются ошибки валидации и состояния — выводится сообщение и пауза
                System.out.println("Error: " + e.getMessage());
                console.pause();
            }
        }
    }

    // Метод login — выполняет вход пользователя в систему по логину и паролю
    private void login() {
        // Выводится заголовок раздела
        System.out.println();
        System.out.println("Login");
        // Считывается логин (непустая строка)
        String username = console.readNonBlankString("Username: ");
        // Считывается пароль
        String password = console.readString("Password: ");
        // Вызывается метод login сервиса authService — возвращается объект User
        User user = authService.login(username, password);
        // Вызывается метод login сессии — пользователь становится текущим
        session.login(user);
        // Выводится приветственное сообщение
        System.out.println("Welcome, " + user.getUsername() + "!");
    }

    // Метод register — регистрирует нового пользователя типа CustomerUser
    private void register() {
        System.out.println();
        System.out.println("Register");

        Long id = console.readLong("Enter user ID: ");
        String username = console.readNonBlankString("Enter username: ");
        String email = console.readEmail("Enter email: ");
        String password = console.readPassword("Enter password: ");
        String confirmPassword = console.readPassword("Confirm password: ");

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Registration cancelled.");
            console.pause();
            return;
        }

        // Передаём все 7 аргументов: role = CUSTOMER, fullName = username, specialization = null
        User user = authService.register(id, username, email, password,
                UserRole.CUSTOMER, username, null);

        session.login(user);
        System.out.println("Registration successful!");
        System.out.println("Welcome, " + user.getUsername() + "!");
    }
}
