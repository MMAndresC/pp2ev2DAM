package com.svalero.musicrxjava.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    private long id;

    private String title;

    private String releaseDate;

    private String front;

    private String genre;

    private int tracks;

    private double duration;

    private String label;

    private boolean platinum;

    private Artist artist;
}
