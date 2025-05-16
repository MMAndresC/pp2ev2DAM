package com.svalero.musicApi.exception;

public class ArtistNotFoundException extends Exception{
    public ArtistNotFoundException() {
        super("Artist not found");
    }

    public ArtistNotFoundException(String message) {
        super(message);
    }
}
