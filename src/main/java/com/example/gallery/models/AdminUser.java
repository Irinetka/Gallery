package com.example.gallery.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
public class AdminUser extends User {



    public AdminUser(Long id, String username, String email, String password) {
        super(id, username, email, password, UserRole.ADMIN);
    }

    @Override public String getDisplayName() { return "Admin: " + getUsername(); }
    @Override public String getRoleDescription() { return "Full system access"; }
    @Override public boolean canManageExhibits() { return true; }
    @Override public boolean canManageExhibitions() { return true; }
    @Override public boolean canManageUsers() { return true; }
    @Override public boolean canBuyTickets() { return true; }
    @Override public boolean canWriteReviews() { return true; }
}