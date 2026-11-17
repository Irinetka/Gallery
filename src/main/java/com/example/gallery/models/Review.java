package com.example.gallery.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Класс Review — модель отзыва
// Применяется аннотация @Getter — автоматически генерируются геттеры для всех полей
@Getter
public class Review {

    // Поле id — идентификатор отзыва
    // Применяется аннотация @Setter — автоматически генерируется сеттер
    @Setter
    private Long id;

    // Поле visitor — ссылка на пользователя, написавшего отзыв (final, сеттер не генерируется)
    private final User visitor;

    // Поле exhibition — ссылка на выставку
    // Применяется аннотация @Setter — генерируется сеттер
    @Setter
    private Exhibition exhibition;

    // Поле content — текст отзыва (final, сеттер не генерируется)
    private final String content;

    // Поле rating — оценка от 1 до 5 (final, сеттер не генерируется)
    private final Integer rating;

    // Поле createdAt — дата и время создания отзыва (final)
    private final LocalDateTime createdAt;

    // Конструктор с параметрами — инициализируются все поля, createdAt устанавливается вызовом LocalDateTime.now()
    public Review(Long id, User visitor, Exhibition exhibition, String content, Integer rating) {
        this.id = id;
        this.visitor = visitor;
        this.exhibition = exhibition;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }
}
