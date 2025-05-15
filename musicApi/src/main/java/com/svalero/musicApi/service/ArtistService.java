package com.svalero.musicApi.service;

import com.svalero.musicApi.domain.Artist;
import com.svalero.musicApi.repository.ArtistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    private final ModelMapper modelMapper;

    public ArtistService(ArtistRepository artistRepository, ModelMapper modelMapper) {
        this.artistRepository = artistRepository;
        this.modelMapper = modelMapper;
    }

    public List<Artist> getAll(){
        return this.artistRepository.findAll();
    }
}
