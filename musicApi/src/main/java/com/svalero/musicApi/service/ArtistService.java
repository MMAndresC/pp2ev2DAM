package com.svalero.musicApi.service;

import com.svalero.musicApi.domain.Artist;
import com.svalero.musicApi.domain.ArtistInDto;
import com.svalero.musicApi.exception.ArtistNotFoundException;
import com.svalero.musicApi.repository.ArtistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Artist getById(long id) throws ArtistNotFoundException {
        return this.artistRepository.findById(id).orElseThrow(ArtistNotFoundException::new);
    }

    public Artist add(ArtistInDto newArtist){
        Artist artist = modelMapper.map(newArtist, Artist.class);
        artist.setRegistrationDate(LocalDate.now());
        return this.artistRepository.save(artist);
    }

    public void delete(long id) throws ArtistNotFoundException {
        Artist artist = this.artistRepository.findById(id).orElseThrow(ArtistNotFoundException::new);
        this.artistRepository.delete(artist);
    }

    public Artist modify(long id, ArtistInDto artistInDto) throws ArtistNotFoundException {
        Artist artist = this.artistRepository.findById(id).orElseThrow(ArtistNotFoundException::new);
        modelMapper.map(artistInDto, artist);
        this.artistRepository.save(artist);
        return artist;
    }
}
