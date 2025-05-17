package com.svalero.musicApi.repository;

import com.svalero.musicApi.domain.Album;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {

    @NonNull
    List<Album> findAll();

    List<Album> findByArtistId(long idArtist);
}
