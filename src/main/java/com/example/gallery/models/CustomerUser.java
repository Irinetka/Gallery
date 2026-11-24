package com.example.gallery.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CUSTOMER")
@NoArgsConstructor
public class CustomerUser extends User {

    private String phoneNumber;

    public CustomerUser(Long id, String username, String email, String password, String fullName) {
        super(id, username, email, password, UserRole.CUSTOMER);
        setFullName(fullName);
    }

    public CustomerUser(Long id, String username, String email, String password,
                        String fullName, String phoneNumber) {
        this(id, username, email, password, fullName);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getDisplayName() {
        return "Customer: " + (getFullName() != null ? getFullName() : getUsername());
    }

    @Override
    public String getRoleDescription() {
        return "Viewing, tickets, reviews";
    }

    @Override
    public boolean canManageExhibits() {
        return false;
    }

    @Override
    public boolean canManageExhibitions() {
        return false;
    }

    @Override
    public boolean canManageUsers() {
        return false;
    }

    @Override
    public boolean canBuyTickets() {
        return true;
    }

    @Override
    public boolean canWriteReviews() {
        return true;
    }
}