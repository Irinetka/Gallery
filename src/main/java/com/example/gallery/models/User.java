package com.example.gallery.models;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private final List<Ticket> tickets = new ArrayList<>();
    private final List<Review> reviews = new ArrayList<>();

    public User() {
    }

    public User(Long id, String username, String email, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public List<Ticket> getTickets() { return tickets; }
    public List<Review> getReviews() { return reviews; }

    public void addTicket(Ticket ticket) { tickets.add(ticket); }
    public void addReview(Review review) { reviews.add(review); }

    public abstract String getDisplayName();
    public abstract String getRoleDescription();
    public abstract boolean canManageExhibits();
    public abstract boolean canManageExhibitions();
    public abstract boolean canManageUsers();
    public abstract boolean canBuyTickets();
    public abstract boolean canWriteReviews();
}