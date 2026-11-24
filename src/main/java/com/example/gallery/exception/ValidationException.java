package com.example.gallery.exception;

public class ValidationException extends GalleryException {

    public ValidationException(String message, String hint) {
        super(message, hint);
    }
}