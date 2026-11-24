package com.example.gallery.exception;

public class EntityNotFoundException extends GalleryException {

    public EntityNotFoundException(String message, String hint) {
        super(message, hint);
    }
}