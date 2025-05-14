package com.svalero.musicApi.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistInDto {

    @NotNull(message = "Artist name is a mandatory field")
    private String name;

    private LocalDate registrationDate;

    private boolean isSoloist;

    private String image;

    private String country;
}
