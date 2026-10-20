package com.example.gallery.models;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private Long id;
    private String fullName;
    private final List<Exhibit> exhibits = new ArrayList<>();

    public Author() {}

    public Author(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public List<Exhibit> getExhibits() { return exhibits; }
    public void addExhibit(Exhibit exhibit) { exhibits.add(exhibit); }
}