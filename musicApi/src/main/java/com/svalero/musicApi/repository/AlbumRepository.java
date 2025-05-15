package com.svalero.musicApi.repository;

import com.svalero.musicApi.domain.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {

    @NonNull
    List<Album> findAll();
}
