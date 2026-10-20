package com.example.gallery.models;

import java.util.ArrayList;
import java.util.List;

public class Exhibit {
    private Long id;
    private String title;
    private String inventoryNumber;
    private Integer year;
    private String size;
    private String material;
    private String technique;
    private String location;
    private Author author;
    private Category category;
    private final List<Exhibition> exhibitions = new ArrayList<>();
    private final List<Review> reviews = new ArrayList<>();

    public Exhibit() {}

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getInventoryNumber() { return inventoryNumber; }
    public void setInventoryNumber(String inventoryNumber) { this.inventoryNumber = inventoryNumber; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getTechnique() { return technique; }
    public void setTechnique(String technique) { this.technique = technique; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public List<Exhibition> getExhibitions() { return exhibitions; }
    public List<Review> getReviews() { return reviews; }
    public void addReview(Review review) { reviews.add(review); }
}