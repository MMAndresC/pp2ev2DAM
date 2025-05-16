package com.svalero.musicApi.service;

import com.svalero.musicApi.domain.Album;
import com.svalero.musicApi.domain.AlbumInDto;
import com.svalero.musicApi.domain.AlbumModifyDto;
import com.svalero.musicApi.domain.Artist;
import com.svalero.musicApi.exception.AlbumNotFoundException;
import com.svalero.musicApi.exception.ArtistNotFoundException;
import com.svalero.musicApi.repository.AlbumRepository;
import com.svalero.musicApi.repository.ArtistRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    private final ArtistRepository artistRepository;

    private ModelMapper modelMapper;

    public AlbumService(AlbumRepository albumRepository, ModelMapper modelMapper, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
        this.artistRepository = artistRepository;
    }

    public List<Album> getAll(){
        return this.albumRepository.findAll();
    }

    public Album getById(long id) throws AlbumNotFoundException {
        return this.albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);
    }

    public Album add(AlbumInDto newAlbum, long idArtist) throws ArtistNotFoundException {
        Artist artist = this.artistRepository.findById(idArtist).orElseThrow(ArtistNotFoundException::new);
        Album album = modelMapper.map(newAlbum, Album.class);
        album.setArtist(artist);
        return this.albumRepository.save(album);
    }

    public void delete(long id) throws AlbumNotFoundException {
        Album album = this.albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);
        this.albumRepository.delete(album);
    }

    public Album modify(long id, AlbumModifyDto albumModifyDto) throws AlbumNotFoundException, ArtistNotFoundException {
        Album album = this.albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);
       //if album changes of artist
        long idArtist = albumModifyDto.getIdArtist();
        if(album.getArtist().getId() != idArtist){
            Artist artist = this.artistRepository.findById(idArtist).orElseThrow(ArtistNotFoundException::new);
            album.setArtist(artist);
        }
        //prevents it from trying to set the idArtist as the album id
        //for some reason it was mapping the idArtist as album id
        modelMapper.typeMap(AlbumModifyDto.class, Album.class)
                .addMappings(mapper -> mapper.skip(Album::setId));
        modelMapper.map(albumModifyDto, album);
        this.albumRepository.save(album);
        return album;
    }
}
