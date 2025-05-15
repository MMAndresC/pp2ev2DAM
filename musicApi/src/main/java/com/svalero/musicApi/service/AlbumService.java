package com.svalero.musicApi.service;

import com.svalero.musicApi.domain.Album;
import com.svalero.musicApi.repository.AlbumRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    private final ModelMapper modelMapper;

    public AlbumService(AlbumRepository albumRepository, ModelMapper modelMapper) {
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
    }

    public List<Album> getAll(){
        return this.albumRepository.findAll();
    }
}
