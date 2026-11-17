package com.example.gallery.models;

// Класс ManagerUser — наследник User, представляет менеджера
public class ManagerUser extends User {

    // Поле fullName — полное имя менеджера
    private String fullName;
    // Поле specialization — специализация менеджера
    private String specialization;

    // Конструктор по умолчанию — вызывается конструктор базового класса
    public ManagerUser() { super(); }

    // Конструктор с параметрами — инициализируются поля, вызывается super с ролью MANAGER
    public ManagerUser(Long id, String username, String email, String password,
                       String fullName, String specialization) {
        super(id, username, email, password, UserRole.MANAGER);
        this.fullName = fullName;
        this.specialization = specialization;
    }

    // Геттер — возвращается fullName
    public String getFullName() { return fullName; }
    // Сеттер — устанавливается fullName
    public void setFullName(String fullName) { this.fullName = fullName; }

    // Геттер — возвращается specialization
    public String getSpecialization() { return specialization; }
    // Сеттер — устанавливается specialization
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    // Переопределён метод getDisplayName — возвращается "Manager: <fullName>" или username
    @Override
    public String getDisplayName() {
        return "Manager: " + (fullName != null ? fullName : getUsername());
    }

    // Переопределён метод getRoleDescription — возвращается описание роли менеджера
    @Override
    public String getRoleDescription() { return "Manages exhibits and exhibitions"; }

    // Переопределён метод canManageExhibits — возвращается true
    @Override public boolean canManageExhibits()     { return true; }
    // Переопределён метод canManageExhibitions — возвращается true
    @Override public boolean canManageExhibitions()  { return true; }
    // Переопределён метод canManageUsers — возвращается false (менеджер не управляет пользователями)
    @Override public boolean canManageUsers()        { return false; }
    // Переопределён метод canBuyTickets — возвращается false
    @Override public boolean canBuyTickets()         { return false; }
    // Переопределён метод canWriteReviews — возвращается false
    @Override public boolean canWriteReviews()       { return false; }
}
