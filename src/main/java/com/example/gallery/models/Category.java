package com.example.gallery.models;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private Long id;
    private String name;
    private final List<Exhibit> exhibits = new ArrayList<>();

    public Category() {}

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Exhibit> getExhibits() { return exhibits; }
    public void addExhibit(Exhibit exhibit) { exhibits.add(exhibit); }
}