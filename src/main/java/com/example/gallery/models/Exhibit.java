package com.example.gallery.models;

import java.util.ArrayList;
import java.util.List;

// Класс Exhibit — модель экспоната (картины, скульптуры и т.д.)
public class Exhibit {

    // Поле id — идентификатор экспоната
    private Long id;
    // Поле title — название экспоната
    private String title;
    // Поле inventoryNumber — инвентарный номер
    private String inventoryNumber;
    // Поле year — год создания
    private Integer year;
    // Поле size — размер
    private String size;
    // Поле material — материал
    private String material;
    // Поле technique — техника исполнения
    private String technique;
    // Поле location — местоположение
    private String location;

    // Поле author — ссылка на автора
    private Author author;
    // Поле category — ссылка на категорию
    private Category category;

    // Поле exhibitions — список выставок, где участвует экспонат
    private final List<Exhibition> exhibitions = new ArrayList<>();
    // Поле reviews — список отзывов об экспонате
    private final List<Review> reviews = new ArrayList<>();

    // Конструктор по умолчанию
    public Exhibit() {}

    // Конструктор с параметрами — инициализируются все основные поля
    public Exhibit(Long id, String title, String inventoryNumber, Integer year,
                   String size, String material, String technique, String location,
                   Author author, Category category) {
        this.id = id;
        this.title = title;
        this.inventoryNumber = inventoryNumber;
        this.year = year;
        this.size = size;
        this.material = material;
        this.technique = technique;
        this.location = location;
        this.author = author;
        this.category = category;
    }

    // Геттер — возвращается id
    public Long getId() { return id; }
    // Сеттер — устанавливается id
    public void setId(Long id) { this.id = id; }

    // Геттер — возвращается title
    public String getTitle() { return title; }
    // Сеттер — устанавливается title
    public void setTitle(String title) { this.title = title; }

    // Геттер — возвращается inventoryNumber
    public String getInventoryNumber() { return inventoryNumber; }
    // Сеттер — устанавливается inventoryNumber
    public void setInventoryNumber(String inventoryNumber) { this.inventoryNumber = inventoryNumber; }

    // Геттер — возвращается year
    public Integer getYear() { return year; }
    // Сеттер — устанавливается year
    public void setYear(Integer year) { this.year = year; }

    // Геттер — возвращается size
    public String getSize() { return size; }
    // Сеттер — устанавливается size
    public void setSize(String size) { this.size = size; }

    // Геттер — возвращается material
    public String getMaterial() { return material; }
    // Сеттер — устанавливается material
    public void setMaterial(String material) { this.material = material; }

    // Геттер — возвращается technique
    public String getTechnique() { return technique; }
    // Сеттер — устанавливается technique
    public void setTechnique(String technique) { this.technique = technique; }

    // Геттер — возвращается location
    public String getLocation() { return location; }
    // Сеттер — устанавливается location
    public void setLocation(String location) { this.location = location; }

    // Геттер — возвращается author
    public Author getAuthor() { return author; }
    // Сеттер — устанавливается author
    public void setAuthor(Author author) { this.author = author; }

    // Геттер — возвращается category
    public Category getCategory() { return category; }
    // Сеттер — устанавливается category
    public void setCategory(Category category) { this.category = category; }

    // Геттер — возвращается список exhibitions
    public List<Exhibition> getExhibitions() { return exhibitions; }
    // Геттер — возвращается список reviews
    public List<Review> getReviews() { return reviews; }

    // Метод — добавляется review в список reviews
    public void addReview(Review review) { reviews.add(review); }
}