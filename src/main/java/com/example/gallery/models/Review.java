package com.example.gallery.models;

import java.time.LocalDateTime;

public class Review {
    private Long id;
    private User visitor;
    private Exhibition exhibition;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;

    public Review() {}

    public Review(Long id, User visitor, Exhibition exhibition, String content, Integer rating) {
        this.id = id;
        this.visitor = visitor;
        this.exhibition = exhibition;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getVisitor() { return visitor; }
    public void setVisitor(User visitor) { this.visitor = visitor; }
    public Exhibition getExhibition() { return exhibition; }
    public void setExhibition(Exhibition exhibition) { this.exhibition = exhibition; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}