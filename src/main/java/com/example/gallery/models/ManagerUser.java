package com.example.gallery.models;

public class ManagerUser extends User {
    private String fullName;
    private String specialization;

    public ManagerUser() { super(); }

    public ManagerUser(Long id, String username, String email, String password,
                       String fullName, String specialization) {
        super(id, username, email, password, UserRole.MANAGER);
        this.fullName = fullName;
        this.specialization = specialization;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    @Override
    public String getDisplayName() { return "Manager: " + (fullName != null ? fullName : getUsername()); }

    @Override
    public String getRoleDescription() { return "Manages exhibits and exhibitions"; }

    @Override public boolean canManageExhibits() { return true; }
    @Override public boolean canManageExhibitions() { return true; }
    @Override public boolean canManageUsers() { return false; }
    @Override public boolean canBuyTickets() { return false; }
    @Override public boolean canWriteReviews() { return false; }
}