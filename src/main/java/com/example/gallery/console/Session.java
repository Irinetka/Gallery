package com.example.gallery.console;

import com.example.gallery.exception.AccessDeniedException;
import com.example.gallery.exception.ValidationException;
import com.example.gallery.models.User;

public class Session {
    private User currentUser;

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        if (!isAuthenticated())
            throw new AccessDeniedException("User is not authenticated",
                    "Log in first via the Login menu");
        return currentUser;
    }

    public void login(User user) {
        if (user == null)
            throw new ValidationException("User cannot be null",
                    "Provide a valid user object from AuthService");
        currentUser = user;
    }

    public void logout() {
        currentUser = null;
    }
}