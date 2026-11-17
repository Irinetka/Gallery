package com.example.gallery.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Класс Exhibition — модель выставки
public class Exhibition {

    // Поле id — идентификатор выставки
    private Long id;
    // Поле title — название выставки
    private String title;
    // Поле type — тип выставки (PERMANENT / TEMPORARY)
    private ExhibitionType type;
    // Поле location — место проведения
    private String location;
    // Поле startDate — дата начала (для временной выставки)
    private LocalDate startDate;
    // Поле endDate — дата окончания (для временной выставки)
    private LocalDate endDate;

    // Поле exhibits — список экспонатов выставки
    private final List<Exhibit> exhibits = new ArrayList<>();
    // Поле tickets — список проданных билетов
    private final List<Ticket> tickets = new ArrayList<>();
    // Поле reviews — список отзывов о выставке
    private final List<Review> reviews = new ArrayList<>();
    // Поле tags — множество уникальных тегов (HashSet)
    private final Set<String> tags = new HashSet<>();

    // Конструктор по умолчанию
    public Exhibition() {}

    // Конструктор с параметрами — инициализируются основные поля
    public Exhibition(Long id, String title, ExhibitionType type, String location,
                      LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Геттер — возвращается id
    public Long getId() { return id; }
    // Сеттер — устанавливается id
    public void setId(Long id) { this.id = id; }

    // Геттер — возвращается title
    public String getTitle() { return title; }
    // Сеттер — устанавливается title
    public void setTitle(String title) { this.title = title; }

    // Геттер — возвращается type
    public ExhibitionType getType() { return type; }
    // Сеттер — устанавливается type
    public void setType(ExhibitionType type) { this.type = type; }

    // Геттер — возвращается location
    public String getLocation() { return location; }
    // Сеттер — устанавливается location
    public void setLocation(String location) { this.location = location; }

    // Геттер — возвращается startDate
    public LocalDate getStartDate() { return startDate; }
    // Сеттер — устанавливается startDate
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    // Геттер — возвращается endDate
    public LocalDate getEndDate() { return endDate; }
    // Сеттер — устанавливается endDate
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    // Геттер — возвращается список exhibits
    public List<Exhibit> getExhibits() { return exhibits; }
    // Геттер — возвращается список tickets
    public List<Ticket> getTickets() { return tickets; }
    // Геттер — возвращается список reviews
    public List<Review> getReviews() { return reviews; }
    // Геттер — возвращается множество tags
    public Set<String> getTags() { return tags; }

    // Метод — добавляется exhibit в список exhibits
    public void addExhibit(Exhibit exhibit) { exhibits.add(exhibit); }

    // Метод — добавляется review в список reviews
    public void addReview(Review review) { reviews.add(review); }

    // Метод — тег обрезается, приводится к нижнему регистру и добавляется в множество tags
    public void addTag(String tag) { tags.add(tag.trim().toLowerCase()); }
    // Метод — тег удаляется из множества tags
    public void removeTag(String tag) { tags.remove(tag.trim().toLowerCase()); }
    // Метод — проверяется наличие тега в множестве tags, возвращается true/false
    public boolean hasTag(String tag) { return tags.contains(tag.trim().toLowerCase()); }
}
