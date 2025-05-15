package com.svalero.musicApi.controller;

import com.svalero.musicApi.domain.Artist;
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
public class ArtistController {

    private final Logger logger = LoggerFactory.getLogger(ArtistController.class);

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/artists")
    public ResponseEntity<List<Artist>> getAll(){
        this.logger.info("Listing artists...");
        List<Artist> allArtists = this.artistService.getAll();
        this.logger.info("End listing artists");
        return new ResponseEntity<>(allArtists, HttpStatus.OK);
    }
}
