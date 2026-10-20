package com.example.gallery.console;

import com.example.gallery.models.User;
import com.example.gallery.service.AuthService;

public class AuthMenu {
    private final AuthService authService;
    private final Session session;
    private final ConsoleUtils console;

    public AuthMenu(AuthService authService, Session session, ConsoleUtils console) {
        this.authService = authService;
        this.session = session;
        this.console = console;
    }

    public void show() {
        while (!session.isAuthenticated()) {
            System.out.println();
            System.out.println("GALLERY APPLICATION");
            System.out.println();
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            System.out.println();

            String choice = console.readString("Select action: ");

            try {
                switch (choice) {
                    case "1" -> login();
                    case "2" -> register();
                    case "0" -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Unknown command.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
                console.pause();
            }
        }
    }

    private void login() {
        System.out.println();
        System.out.println("Login");
        String username = console.readNonBlankString("Username: ");
        String password = console.readString("Password: ");
        User user = authService.login(username, password);
        session.login(user);
        System.out.println("Welcome, " + user.getUsername() + "!");
    }

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

        User user = authService.register(id, username, email, password);
        session.login(user);
        System.out.println("Registration successful!");
        System.out.println("Welcome, " + user.getUsername() + "!");
    }
}