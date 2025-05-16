package com.svalero.musicApi.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(length = 100)
    private String front;

    @Column(length = 70)
    private String genre;

    @Column
    @Min(1)
    private int tracks;

    @Column
    @Min(0)
    private double duration;

    @Column(length = 70)
    private String label;

    @Column
    @ColumnDefault("false")
    private boolean platinum;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    //Avoids infinite JSON serialisation, as team-player is related and enters a loop
    @JsonManagedReference(value = "artist_albums")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Artist artist;
}
