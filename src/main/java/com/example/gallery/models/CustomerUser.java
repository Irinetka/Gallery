package com.example.gallery.models;

// Класс CustomerUser — наследник User, представляет посетителя
public class CustomerUser extends User {

    // Поле fullName — полное имя посетителя
    private String fullName;
    // Поле phoneNumber — телефон посетителя
    private String phoneNumber;

    // Конструктор по умолчанию — вызывается конструктор базового класса
    public CustomerUser() { super(); }

    // Конструктор с 5 параметрами — вызывается super с ролью CUSTOMER
    public CustomerUser(Long id, String username, String email, String password, String fullName) {
        super(id, username, email, password, UserRole.CUSTOMER);
        this.fullName = fullName;
    }

    // Конструктор с 6 параметрами — дополнительно инициализируется phoneNumber
    public CustomerUser(Long id, String username, String email, String password,
                        String fullName, String phoneNumber) {
        super(id, username, email, password, UserRole.CUSTOMER);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    // Геттер — возвращается fullName
    public String getFullName() { return fullName; }
    // Сеттер — устанавливается fullName
    public void setFullName(String fullName) { this.fullName = fullName; }

    // Геттер — возвращается phoneNumber
    public String getPhoneNumber() { return phoneNumber; }
    // Сеттер — устанавливается phoneNumber
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    // Переопределён метод getDisplayName — возвращается "Customer: <fullName>" или username
    @Override
    public String getDisplayName() {
        return "Customer: " + (fullName != null ? fullName : getUsername());
    }

    // Переопределён метод getRoleDescription — возвращается описание роли посетителя
    @Override
    public String getRoleDescription() { return "Viewing, tickets, reviews"; }

    // Переопределён метод canManageExhibits — возвращается false
    @Override public boolean canManageExhibits()     { return false; }
    // Переопределён метод canManageExhibitions — возвращается false
    @Override public boolean canManageExhibitions()  { return false; }
    // Переопределён метод canManageUsers — возвращается false
    @Override public boolean canManageUsers()        { return false; }
    // Переопределён метод canBuyTickets — возвращается true (посетитель может покупать билеты)
    @Override public boolean canBuyTickets()         { return true; }
    // Переопределён метод canWriteReviews — возвращается true (посетитель может писать отзывы)
    @Override public boolean canWriteReviews()       { return true; }
}