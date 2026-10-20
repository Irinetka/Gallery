package com.example.gallery.console;

import com.example.gallery.models.User;

public class Session {
    private User currentUser;

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        if (!isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        return currentUser;
    }

    public void login(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        currentUser = user;
    }

    public void logout() {
        currentUser = null;
    }
}