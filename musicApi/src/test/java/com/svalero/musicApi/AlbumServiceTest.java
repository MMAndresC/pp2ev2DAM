package com.svalero.musicApi;

import com.svalero.musicApi.domain.Album;
import com.svalero.musicApi.domain.AlbumInDto;
import com.svalero.musicApi.domain.AlbumModifyDto;
import com.svalero.musicApi.domain.Artist;
import com.svalero.musicApi.exception.AlbumNotFoundException;
import com.svalero.musicApi.exception.ArtistNotFoundException;
import com.svalero.musicApi.repository.AlbumRepository;
import com.svalero.musicApi.repository.ArtistRepository;
import com.svalero.musicApi.service.AlbumService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AlbumService albumService;

    private final List<Artist> mockArtists = List.of(
            new Artist(1, "Roxette", LocalDate.parse("2005-02-15"), false, null, "Sweden", null),
            new Artist(2, "Beast in black", LocalDate.parse("2015-04-28"), false, null, "Finland", null),
            new Artist(3, "Olly Alexander", LocalDate.parse("2021-07-03"), true, null, "UK",null)
    );

    private final List<Album> mockAlbums = List.of(
            new Album(1, "Joyride", LocalDate.parse("1985-03-25"), null, "pop", 15, 59.2, "EMI", false, mockArtists.getFirst()),
            new Album(2, "Tourism", LocalDate.parse("1992-08-28"), null, "pop", 16, 70.37, "EMI", false, mockArtists.getFirst()),
            new Album(3, "From hell with love", LocalDate.parse("2019-02-08"), null, "metal", 13, 43.08, "Nuclear Blast", false, mockArtists.get(1)),
            new Album(4, "Polar", LocalDate.parse("2025-02-07"), null, "electropop", 13, 40.15, "Polydor", false, mockArtists.getLast())
    );

    @Test
    public void testGetAllAlbums(){
        when(albumRepository.findAll()).thenReturn(mockAlbums);

        List<Album> result = albumService.getAll();

        assertEquals(4, result.size());
        assertEquals("Joyride", result.getFirst().getTitle());
        assertEquals("Polar", result.getLast().getTitle());

        verify(albumRepository, times(1)).findAll();
    }

    @Test
    public void testGetAlbumById() throws AlbumNotFoundException {
        long id = 1;

        when(albumRepository.findById(id)).thenReturn(Optional.of(mockAlbums.getFirst()));

        Album result = albumService.getById(id);

        assertEquals("Joyride", result.getTitle());
        assertEquals("pop", result.getGenre());

        verify(albumRepository, times(1)).findById(id);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testAddAlbum() throws ArtistNotFoundException{
        long artistId = 1;
        AlbumInDto albumToSave = new AlbumInDto("new album", LocalDate.parse("2024-12-25"),
                null, "rock", 18, 50.45,"Lol records", false );

        Album mappedAlbum = new Album(0, "new album", LocalDate.parse("2024-12-25"),
                null, "rock", 18, 50.45,"Lol records", false, mockArtists.getFirst());

        Album savedAlbum = new Album(5,"new album", LocalDate.parse("2024-12-25"),
                null, "rock", 18, 50.45,"Lol records", false, mockArtists.getFirst());

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(mockArtists.getFirst()));
        when(modelMapper.map(albumToSave, Album.class)).thenReturn(mappedAlbum);
        when(albumRepository.save(mappedAlbum)).thenReturn(savedAlbum);

        Album result = albumService.add(albumToSave, artistId);

        assertEquals(5, result.getId());
        assertEquals("new album", result.getTitle());
        assertEquals("Lol records", result.getLabel());
        assertEquals(1, result.getArtist().getId());

        verify(artistRepository, times(1)).findById(artistId);
        verify(albumRepository, times(1)).save(mappedAlbum);
    }

    @Test
    public void testDeleteAlbum() throws AlbumNotFoundException {
        long id = 4;

        Album albumToDelete = mockAlbums.getLast();

        when(albumRepository.findById(id)).thenReturn(Optional.of(mockAlbums.getLast()));
        doNothing().when(albumRepository).delete(albumToDelete);

        Album result = albumService.getById(id);

        assertEquals(albumToDelete, result);

        albumService.delete(id);

        verify(albumRepository, times(1)).delete(albumToDelete);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testModifyAlbumNoChangingArtist() throws AlbumNotFoundException, ArtistNotFoundException {
        //Inject real model mapper to avoid null pointer exception, problems faking setSkipNullEnabled
        ModelMapper realModelMapper = new ModelMapper();
        realModelMapper.getConfiguration().setSkipNullEnabled(true);
        albumService.setModelMapper(realModelMapper);

        long id = 4;
        Album albumToModify = mockAlbums.getLast();

        AlbumModifyDto updatedData = new AlbumModifyDto(
                albumToModify.getArtist().getId(), "Polaris",  LocalDate.parse("2025-02-07"),
                "new_image.jpg", "electronica", 13, 40.15, "Polydor", false
        );

        Album updatedAlbum = new Album(
                4 ,"Polaris",  LocalDate.parse("2025-02-07"),
                "new_image.jpg", "electronica", 13, 40.15, "Polydor", false, mockArtists.getLast()
        );

        when(albumRepository.findById(id)).thenReturn(Optional.of(albumToModify));
        when(artistRepository.findById(albumToModify.getArtist().getId())).thenReturn(Optional.of(mockArtists.getLast()));
        when(albumRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Album result = albumService.modify(4,updatedData);

        assertEquals("Polaris", result.getTitle());
        assertEquals("electronica", result.getGenre());
        assertEquals("new_image.jpg", result.getFront());
        assertEquals(4, result.getId());
        assertEquals(3, result.getArtist().getId());


        verify(artistRepository, times(0)).findById(Objects.requireNonNull(mockArtists.getLast()).getId());
        verify(albumRepository, times(1)).findById(id);
        verify(albumRepository, times(1)).save(updatedAlbum);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testModifyAlbumChangingArtist() throws AlbumNotFoundException, ArtistNotFoundException {
        //Inject real model mapper to avoid null pointer exception, problems faking setSkipNullEnabled
        ModelMapper realModelMapper = new ModelMapper();
        realModelMapper.getConfiguration().setSkipNullEnabled(true);
        albumService.setModelMapper(realModelMapper);

        long id = 4;
        long artistId = 1;
        Album albumToModify = mockAlbums.getLast();

        AlbumModifyDto updatedData = new AlbumModifyDto(
                artistId, "Polaris",  LocalDate.parse("2025-02-07"),
                "new_image.jpg", "electronica", 13, 40.15, "Polydor", false
        );

        Album updatedAlbum = new Album(
                4 ,"Polaris",  LocalDate.parse("2025-02-07"),
                "new_image.jpg", "electronica", 13, 40.15, "Polydor", false, mockArtists.getFirst()
        );

        when(albumRepository.findById(id)).thenReturn(Optional.of(albumToModify));
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(mockArtists.getFirst()));
        when(albumRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Album result = albumService.modify(4,updatedData);

        assertEquals("Polaris", result.getTitle());
        assertEquals("electronica", result.getGenre());
        assertEquals("new_image.jpg", result.getFront());
        assertEquals(4, result.getId());
        assertEquals(artistId, result.getArtist().getId());


        verify(artistRepository, times(1)).findById(Objects.requireNonNull(mockArtists.getFirst()).getId());
        verify(albumRepository, times(1)).findById(id);
        verify(albumRepository, times(1)).save(updatedAlbum);
    }

    @Test
    public void testAlbumNotFoundException() throws ArtistNotFoundException {
        long id = 79;

        when(albumRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AlbumNotFoundException.class, () -> {
            albumService.getById(id);
        });
    }
}
