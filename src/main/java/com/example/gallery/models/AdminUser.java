package com.example.gallery.models;

// Класс AdminUser — наследник User, представляет администратора
public class AdminUser extends User {

    // Конструктор по умолчанию — вызывается конструктор базового класса
    public AdminUser() { super(); }

    // Конструктор с параметрами — вызывается конструктор User с ролью ADMIN
    public AdminUser(Long id, String username, String email, String password) {
        super(id, username, email, password, UserRole.ADMIN);
    }

    // Переопределён метод getDisplayName — возвращается строка "Admin: <username>"
    @Override
    public String getDisplayName() { return "Admin: " + getUsername(); }

    // Переопределён метод getRoleDescription — возвращается описание роли администратора
    @Override
    public String getRoleDescription() { return "Full system access"; }

    // Переопределён метод canManageExhibits — возвращается true (админ может управлять экспонатами)
    @Override public boolean canManageExhibits()     { return true; }
    // Переопределён метод canManageExhibitions — возвращается true
    @Override public boolean canManageExhibitions()  { return true; }
    // Переопределён метод canManageUsers — возвращается true
    @Override public boolean canManageUsers()        { return true; }
    // Переопределён метод canBuyTickets — возвращается true
    @Override public boolean canBuyTickets()         { return true; }
    // Переопределён метод canWriteReviews — возвращается true
    @Override public boolean canWriteReviews()       { return true; }
}
