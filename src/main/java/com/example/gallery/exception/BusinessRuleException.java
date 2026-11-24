package com.example.gallery.exception;

public class BusinessRuleException extends GalleryException {

    public BusinessRuleException(String message, String hint) {
        super(message, hint);
    }
}