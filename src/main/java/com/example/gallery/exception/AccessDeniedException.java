package com.example.gallery.exception;

public class AccessDeniedException extends GalleryException {

    public AccessDeniedException(String message, String hint) {
        super(message, hint);
    }
}