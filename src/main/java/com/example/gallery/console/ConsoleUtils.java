package com.example.gallery.console;

import java.util.Scanner;

public class ConsoleUtils {
    private final Scanner scanner;

    public ConsoleUtils(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public Long readLong(String message) {
        while (true) {
            try {
                return Long.parseLong(readString(message));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    public Integer readInteger(String message) {
        while (true) {
            try {
                return Integer.parseInt(readString(message));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    public Double readDouble(String message) {
        while (true) {
            try {
                return Double.parseDouble(readString(message));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    public String readPassword(String message) {
        while (true) {
            String password = readString(message);
            if (password == null || password.isBlank()) {
                System.out.println("Password cannot be empty. Try again.");
                continue;
            }
            return password;
        }
    }

    public String readNonBlankString(String message) {
        while (true) {
            String value = readString(message);
            if (value == null || value.isBlank()) {
                System.out.println("Field cannot be empty. Try again.");
                continue;
            }
            return value;
        }
    }

    public String readEmail(String message) {
        while (true) {
            String email = readString(message);
            if (email == null || email.isBlank()) {
                System.out.println("Email cannot be empty. Try again.");
                continue;
            }
            if (!email.contains("@") || !email.contains(".")) {
                System.out.println("Invalid email format. Example: user@example.com");
                continue;
            }
            return email;
        }
    }

    public Integer readIntegerInRange(String message, int min, int max) {
        while (true) {
            Integer value = readInteger(message);
            if (value < min || value > max) {
                System.out.println("Value must be between " + min + " and " + max + ". You entered: " + value);
                continue;
            }
            return value;
        }
    }

    public void pause() {
        System.out.println();
        System.out.println("Press Enter...");
        scanner.nextLine();
    }
}