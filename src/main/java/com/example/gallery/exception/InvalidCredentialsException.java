package com.example.gallery.exception;

public class InvalidCredentialsException extends GalleryException {

    public InvalidCredentialsException(String message, String hint) {
        super(message, hint);
    }
}