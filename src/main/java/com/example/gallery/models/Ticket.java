package com.example.gallery.models;

import java.time.LocalDateTime;

public class Ticket {
    private Long id;
    private User visitor;
    private Exhibition exhibition;
    private LocalDateTime visitDateTime;
    private Double price;

    public Ticket() {}

    public Ticket(Long id, User visitor, Exhibition exhibition,
                  LocalDateTime visitDateTime, Double price) {
        this.id = id;
        this.visitor = visitor;
        this.exhibition = exhibition;
        this.visitDateTime = visitDateTime;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getVisitor() { return visitor; }
    public void setVisitor(User visitor) { this.visitor = visitor; }
    public Exhibition getExhibition() { return exhibition; }
    public void setExhibition(Exhibition exhibition) { this.exhibition = exhibition; }
    public LocalDateTime getVisitDateTime() { return visitDateTime; }
    public void setVisitDateTime(LocalDateTime visitDateTime) { this.visitDateTime = visitDateTime; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}