package com.example.gallery.models;

import java.time.LocalDateTime;

// Класс Ticket — модель билета на выставку
public class Ticket {

    // Поле id — идентификатор билета
    private Long id;
    // Поле visitor — ссылка на посетителя, купившего билет
    private User visitor;
    // Поле exhibition — ссылка на выставку
    private Exhibition exhibition;
    // Поле visitDateTime — дата и время визита
    private LocalDateTime visitDateTime;
    // Поле price — цена билета
    private Double price;

    // Конструктор по умолчанию
    public Ticket() {}

    // Конструктор с параметрами — инициализируются все поля
    public Ticket(Long id, User visitor, Exhibition exhibition,
                  LocalDateTime visitDateTime, Double price) {
        this.id = id;
        this.visitor = visitor;
        this.exhibition = exhibition;
        this.visitDateTime = visitDateTime;
        this.price = price;
    }

    // Геттер — возвращается id
    public Long getId() { return id; }
    // Сеттер — устанавливается id
    public void setId(Long id) { this.id = id; }

    // Геттер — возвращается visitor
    public User getVisitor() { return visitor; }
    // Сеттер — устанавливается visitor
    public void setVisitor(User visitor) { this.visitor = visitor; }

    // Геттер — возвращается exhibition
    public Exhibition getExhibition() { return exhibition; }
    // Сеттер — устанавливается exhibition
    public void setExhibition(Exhibition exhibition) { this.exhibition = exhibition; }

    // Геттер — возвращается visitDateTime
    public LocalDateTime getVisitDateTime() { return visitDateTime; }
    // Сеттер — устанавливается visitDateTime
    public void setVisitDateTime(LocalDateTime visitDateTime) { this.visitDateTime = visitDateTime; }

    // Геттер — возвращается price
    public Double getPrice() { return price; }
    // Сеттер — устанавливается price
    public void setPrice(Double price) { this.price = price; }
}
