package com.example.gallery.exception;

public class DuplicateEntityException extends GalleryException {

    public DuplicateEntityException(String message, String hint) {
        super(message, hint);
    }
}