package com.svalero.musicrxjava.utils;

import lombok.Data;

@Data
public class SessionManager {

    private static SessionManager instance;
    private String authToken;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
}
