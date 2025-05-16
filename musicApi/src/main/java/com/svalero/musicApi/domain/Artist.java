package com.svalero.musicApi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 70)
    private String name;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "is_soloist")
    @ColumnDefault("true")
    private boolean isSoloist;

    @Column(length = 100)
    private String image;

    @Column(length = 30)
    private String country;

    @OneToMany(mappedBy = "artist")
    //Has to match the @JsonManagedReference value of the other related table
    @JsonBackReference(value = "artist_albums")
    private List<Album> albums;
}
