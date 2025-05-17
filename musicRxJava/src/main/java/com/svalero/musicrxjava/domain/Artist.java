package com.svalero.musicrxjava.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Artist {

    private long id;

    private String name;

    private String registrationDate;

    private boolean isSoloist;

    private String image;

    private String country;
}
