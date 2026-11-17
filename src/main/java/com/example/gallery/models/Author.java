package com.example.gallery.models;

import java.util.ArrayList;
import java.util.List;

// Класс Author — модель автора произведения
public class Author {

    // Поле id — идентификатор автора
    private Long id;
    // Поле fullName — полное имя автора
    private String fullName;
    // Поле exhibits — список экспонатов автора (инициализируется пустым ArrayList)
    private final List<Exhibit> exhibits = new ArrayList<>();

    // Конструктор по умолчанию
    public Author() {}

    // Конструктор с параметрами — инициализируются id и fullName
    public Author(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    // Геттер — возвращается id
    public Long getId() { return id; }
    // Сеттер — устанавливается id
    public void setId(Long id) { this.id = id; }

    // Геттер — возвращается fullName
    public String getFullName() { return fullName; }
    // Сеттер — устанавливается fullName
    public void setFullName(String fullName) { this.fullName = fullName; }

    // Геттер — возвращается список exhibits
    public List<Exhibit> getExhibits() { return exhibits; }

    // Метод — добавляется exhibit в список exhibits
    public void addExhibit(Exhibit exhibit) { exhibits.add(exhibit); }
}