package com.example.gallery.models;

import java.util.ArrayList;
import java.util.List;

// Абстрактный класс User — базовый для всех типов пользователей
public abstract class User {

    // Поле id — уникальный идентификатор пользователя
    private Long id;
    // Поле username — логин для входа
    private String username;
    // Поле email — электронная почта
    private String email;
    // Поле password — пароль
    private String password;
    // Поле role — роль пользователя (ADMIN / MANAGER / CUSTOMER)
    private UserRole role;

    // Поле tickets — список купленных билетов (инициализируется пустым ArrayList)
    private final List<Ticket> tickets = new ArrayList<>();
    // Поле reviews — список написанных отзывов (инициализируется пустым ArrayList)
    private final List<Review> reviews = new ArrayList<>();

    // Конструктор по умолчанию
    public User() {
    }

    // Конструктор с параметрами — инициализируются все основные поля
    public User(Long id, String username, String email, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Геттер — возвращается id пользователя
    public Long getId() { return id; }
    // Сеттер — устанавливается id пользователя
    public void setId(Long id) { this.id = id; }

    // Геттер — возвращается username
    public String getUsername() { return username; }
    // Сеттер — устанавливается username
    public void setUsername(String username) { this.username = username; }

    // Геттер — возвращается email
    public String getEmail() { return email; }
    // Сеттер — устанавливается email
    public void setEmail(String email) { this.email = email; }

    // Геттер — возвращается password
    public String getPassword() { return password; }
    // Сеттер — устанавливается password
    public void setPassword(String password) { this.password = password; }

    // Геттер — возвращается role
    public UserRole getRole() { return role; }
    // Сеттер — устанавливается role
    public void setRole(UserRole role) { this.role = role; }

    // Геттер — возвращается список tickets
    public List<Ticket> getTickets() { return tickets; }
    // Геттер — возвращается список reviews
    public List<Review> getReviews() { return reviews; }

    // Метод — добавляется ticket в список tickets
    public void addTicket(Ticket ticket) { tickets.add(ticket); }
    // Метод — добавляется review в список reviews
    public void addReview(Review review) { reviews.add(review); }

    // Абстрактный метод — переопределяется в наследниках, возвращается отображаемое имя
    public abstract String getDisplayName();
    // Абстрактный метод — переопределяется в наследниках, возвращается описание роли
    public abstract String getRoleDescription();
    // Абстрактный метод — переопределяется в наследниках, проверяется право управления экспонатами
    public abstract boolean canManageExhibits();
    // Абстрактный метод — переопределяется в наследниках, проверяется право управления выставками
    public abstract boolean canManageExhibitions();
    // Абстрактный метод — переопределяется в наследниках, проверяется право управления пользователями
    public abstract boolean canManageUsers();
    // Абстрактный метод — переопределяется в наследниках, проверяется право покупки билетов
    public abstract boolean canBuyTickets();
    // Абстрактный метод — переопределяется в наследниках, проверяется право написания отзывов
    public abstract boolean canWriteReviews();
}
