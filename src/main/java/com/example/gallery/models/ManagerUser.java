package com.example.gallery.models;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("MANAGER")
@NoArgsConstructor
public class ManagerUser extends User {

    private String specialization;

    public ManagerUser(Long id, String username, String email, String password,
                       String fullName, String specialization) {
        super(id, username, email, password, UserRole.MANAGER);
        setFullName(fullName);
        this.specialization = specialization;
    }

    @Override public String getDisplayName() {
        return "Manager: " + (getFullName() != null ? getFullName() : getUsername());
    }
    @Override public String getRoleDescription() { return "Manages exhibits and exhibitions"; }
    @Override public boolean canManageExhibits() { return true; }
    @Override public boolean canManageExhibitions() { return true; }
    @Override public boolean canManageUsers() { return false; }
    @Override public boolean canBuyTickets() { return false; }
    @Override public boolean canWriteReviews() { return false; }
}