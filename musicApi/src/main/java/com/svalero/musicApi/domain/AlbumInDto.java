package com.svalero.musicApi.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumInDto {

    @NotNull(message = "Album title is a mandatory field")
    private String title;

    @NotNull(message = "Album release date is a mandatory field")
    private LocalDate releaseDate;

    private String front;

    private String genre;

    @Min(value = 1)
    private int tracks;

    @Min(value = 0)
    private double duration;

    private String label;

    private boolean platinum;
}
