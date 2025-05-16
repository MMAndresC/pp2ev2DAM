package com.svalero.musicApi.exception;

public class AlbumNotFoundException extends Exception {
    public AlbumNotFoundException() {
        super("Album not found");
    }

    public AlbumNotFoundException(String message) {
        super(message);
    }
}
