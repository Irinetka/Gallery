package com.example.gallery.exception;


public abstract class GalleryException extends RuntimeException {

    private final String hint;

    public GalleryException(String message, String hint) {
        super(message);
        this.hint = hint;
    }

    // Подробное описание способа решения проблемы
    public String getHint() {
        return hint;
    }
}