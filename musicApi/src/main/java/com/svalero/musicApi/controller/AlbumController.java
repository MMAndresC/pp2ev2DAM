package com.svalero.musicApi.controller;

import com.svalero.musicApi.domain.Album;
import com.svalero.musicApi.domain.AlbumInDto;
import com.svalero.musicApi.domain.AlbumModifyDto;
import com.svalero.musicApi.domain.ErrorResponse;
import com.svalero.musicApi.exception.AlbumNotFoundException;
import com.svalero.musicApi.exception.ArtistNotFoundException;
import com.svalero.musicApi.service.AlbumService;
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

    @GetMapping("/albums/{id}")
    public ResponseEntity<Album> getById(@PathVariable long id) throws AlbumNotFoundException {
        this.logger.info("Searching album by Id...");
        Album album = this.albumService.getById(id);
        this.logger.info("Album found");
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PostMapping("/artists/{idArtist}/albums")
    public ResponseEntity<Album> add(@PathVariable long idArtist, @Valid @RequestBody AlbumInDto albumInDto) throws ArtistNotFoundException {
        this.logger.info("Adding a new album...");
        Album album = this.albumService.add(albumInDto, idArtist);
        this.logger.info("Album added");
        return new ResponseEntity<>(album, HttpStatus.CREATED);
    }

    @DeleteMapping("/albums/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) throws AlbumNotFoundException {
        this.logger.info("Deleting an album by id...");
        this.albumService.delete(id);
        this.logger.info("Album deleted");
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/albums/{id}")
    public ResponseEntity<Album> modify(@PathVariable long id, @Valid @RequestBody AlbumModifyDto albumModifyDto) throws AlbumNotFoundException, ArtistNotFoundException {
        this.logger.info("Modifying an album...");
        Album modifiedAlbum = this.albumService.modify(id, albumModifyDto);
        this.logger.info("Album modified");
        return new ResponseEntity<>(modifiedAlbum, HttpStatus.OK);
    }

    //Exceptions
    @ExceptionHandler(AlbumNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAlbumNotFoundException(AlbumNotFoundException exception) {
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
