package com.svalero.musicApi.controller;

import com.svalero.musicApi.domain.Artist;
import com.svalero.musicApi.domain.ArtistInDto;
import com.svalero.musicApi.domain.ErrorResponse;
import com.svalero.musicApi.exception.ArtistNotFoundException;
import com.svalero.musicApi.service.ArtistService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getById(@PathVariable long id) throws ArtistNotFoundException {
        this.logger.info("Searching artist by Id...");
        Artist artist = this.artistService.getById(id);
        this.logger.info("Artist found");
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @PostMapping("/artists")
    public ResponseEntity<Artist> add(@Valid @RequestBody ArtistInDto artistInDto){
        this.logger.info("Adding a new artist...");
        Artist artist = this.artistService.add(artistInDto);
        this.logger.info("Artist added");
        return new ResponseEntity<>(artist, HttpStatus.CREATED);
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) throws ArtistNotFoundException {
        this.logger.info("Deleting a artist by id...");
        this.artistService.delete(id);
        this.logger.info("Artist deleted");
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> modify(@PathVariable long id, @Valid @RequestBody ArtistInDto artistInDto) throws ArtistNotFoundException {
        this.logger.info("Modifying a artist...");
        Artist modifiedArtist = this.artistService.modify(id, artistInDto);
        this.logger.info("Artist modified");
        return new ResponseEntity<>(modifiedArtist, HttpStatus.OK);
    }

    //Exceptions
    @ExceptionHandler(ArtistNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleArtistNotFoundException(ArtistNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        this.logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        this.logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(ErrorResponse.validationError(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = ErrorResponse.generalError(500, "Internal Server Error");
        this.logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
