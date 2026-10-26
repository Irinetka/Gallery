package com.example.gallery.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Review {
    @Setter
    private Long id;
    private final User visitor;
    //public void setVisitor(User visitor) { this.visitor = visitor; }
    @Setter
    private Exhibition exhibition;
    private final String content;
    //public void setContent(String content) { this.content = content; }
    private final Integer rating;
    //public void setRating(Integer rating) { this.rating = rating; }
    private final LocalDateTime createdAt;

    public Review(Long id, User visitor, Exhibition exhibition, String content, Integer rating) {
        this.id = id;
        this.visitor = visitor;
        this.exhibition = exhibition;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }

    // public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}