package com.example.gallery.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Exhibition {
    private Long id;
    private String title;
    private ExhibitionType type;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private final List<Exhibit> exhibits = new ArrayList<>();
    private final List<Ticket> tickets = new ArrayList<>();
    private final List<Review> reviews = new ArrayList<>();
    private final Set<String> tags = new HashSet<>();

    public Exhibition() {}

    public Exhibition(Long id, String title, ExhibitionType type, String location,
                      LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public ExhibitionType getType() { return type; }
    public void setType(ExhibitionType type) { this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public List<Exhibit> getExhibits() { return exhibits; }
    public List<Ticket> getTickets() { return tickets; }
    public List<Review> getReviews() { return reviews; }
    public Set<String> getTags() { return tags; }

    public void addExhibit(Exhibit exhibit) { exhibits.add(exhibit); }
    public void addReview(Review review) { reviews.add(review); }
    public void addTag(String tag) { tags.add(tag.trim().toLowerCase()); }
    public void removeTag(String tag) { tags.remove(tag.trim().toLowerCase()); }
    public boolean hasTag(String tag) { return tags.contains(tag.trim().toLowerCase()); }
}