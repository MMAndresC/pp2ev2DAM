package com.svalero.musicApi.controller;

import com.svalero.musicApi.domain.Album;
import com.svalero.musicApi.domain.Artist;
import com.svalero.musicApi.service.AlbumService;
import com.svalero.musicApi.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AlbumController {

    private final Logger logger = LoggerFactory.getLogger(AlbumController.class);

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/albums")
    public ResponseEntity<List<Album>> getAll(){
        this.logger.info("Listing albums...");
        List<Album> allArtists = this.albumService.getAll();
        this.logger.info("End listing albums");
        return new ResponseEntity<>(allArtists, HttpStatus.OK);
    }
}
