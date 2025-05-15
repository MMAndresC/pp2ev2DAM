package com.svalero.musicApi.repository;

import com.svalero.musicApi.domain.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Long> {

    @NonNull
    List<Artist> findAll();
}
