package com.example.gallery.models;

public class CustomerUser extends User {
    private String fullName;
    private String phoneNumber;

    public CustomerUser() { super(); }

    public CustomerUser(Long id, String username, String email, String password, String fullName) {
        super(id, username, email, password, UserRole.CUSTOMER);
        this.fullName = fullName;
    }

    public CustomerUser(Long id, String username, String email, String password,
                        String fullName, String phoneNumber) {
        super(id, username, email, password, UserRole.CUSTOMER);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String getDisplayName() { return "Customer: " + (fullName != null ? fullName : getUsername()); }

    @Override
    public String getRoleDescription() { return "Viewing, tickets, reviews"; }

    @Override public boolean canManageExhibits() { return false; }
    @Override public boolean canManageExhibitions() { return false; }
    @Override public boolean canManageUsers() { return false; }
    @Override public boolean canBuyTickets() { return true; }
    @Override public boolean canWriteReviews() { return true; }
}