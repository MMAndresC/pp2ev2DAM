package com.svalero.musicApi.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumModifyDto extends AlbumInDto{

    @NotNull(message = "Artist id required")
    private long idArtist;

    public AlbumModifyDto(long idArtist, String title, LocalDate releaseDate, String front, String genre, int tracks, double duration, String label, boolean platinum) {
        super(title, releaseDate, front, genre, tracks, duration, label, platinum);
        this.idArtist = idArtist;
    }
}
