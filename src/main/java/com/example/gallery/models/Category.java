package com.example.gallery.models;

import java.util.ArrayList;
import java.util.List;

// Класс Category — модель категории экспоната
public class Category {

    // Поле id — идентификатор категории
    private Long id;
    // Поле name — название категории
    private String name;
    // Поле exhibits — список экспонатов данной категории
    private final List<Exhibit> exhibits = new ArrayList<>();

    // Конструктор по умолчанию
    public Category() {}

    // Конструктор с параметрами — инициализируются id и name
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Геттер — возвращается id
    public Long getId() { return id; }
    // Сеттер — устанавливается id
    public void setId(Long id) { this.id = id; }

    // Геттер — возвращается name
    public String getName() { return name; }
    // Сеттер — устанавливается name
    public void setName(String name) { this.name = name; }

    // Геттер — возвращается список exhibits
    public List<Exhibit> getExhibits() { return exhibits; }

    // Метод — добавляется exhibit в список exhibits
    public void addExhibit(Exhibit exhibit) { exhibits.add(exhibit); }
}
