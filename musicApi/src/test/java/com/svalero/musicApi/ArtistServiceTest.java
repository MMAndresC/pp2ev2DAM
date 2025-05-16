package com.svalero.musicApi;

import com.svalero.musicApi.domain.Artist;
import com.svalero.musicApi.domain.ArtistInDto;
import com.svalero.musicApi.exception.ArtistNotFoundException;
import com.svalero.musicApi.repository.ArtistRepository;
import com.svalero.musicApi.service.ArtistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ArtistService artistService;

    private final List<Artist> mockArtists = List.of(
            new Artist(1, "Roxette", LocalDate.parse("2005-02-15"), false, null, "Sweden", null),
            new Artist(2, "Beast in black", LocalDate.parse("2015-04-28"), false, null, "Finland", null),
            new Artist(3, "Olly Alexander", LocalDate.parse("2021-07-03"), true, null, "UK",null)
    );

    @Test
    public void testGetAllArtists(){
        when(artistRepository.findAll()).thenReturn(mockArtists);

        List<Artist> result = artistService.getAll();

        assertEquals(3, result.size());
        assertEquals("Roxette", result.getFirst().getName());
        assertEquals("Olly Alexander", result.getLast().getName());

        verify(artistRepository, times(1)).findAll();
    }

    @Test
    public void testGetArtistById() throws ArtistNotFoundException {
        long id = 1;

        when(artistRepository.findById(id)).thenReturn(Optional.of(mockArtists.getFirst()));

        Artist result = artistService.getById(id);

        assertEquals(mockArtists.getFirst().getName(), result.getName());
        assertEquals(mockArtists.getFirst().getCountry(), result.getCountry());

        verify(artistRepository, times(1)).findById(id);
    }

    @Test
    public void testAddArtist() {
        ArtistInDto artistToSave = new ArtistInDto("new artist", true,
                null, "country");

        Artist mappedArtist = new Artist(0, "new artist", LocalDate.now()
                ,true, null, "country", null);

        Artist savedArtist = new Artist(4, "new artist", LocalDate.now()
                ,true, null, "country", null);

        when(modelMapper.map(artistToSave, Artist.class)).thenReturn(mappedArtist);
        when(artistRepository.save(mappedArtist)).thenReturn(savedArtist);

        Artist result = artistService.add(artistToSave);

        assertEquals(savedArtist.getName(), result.getName());
        verify(modelMapper).map(eq(artistToSave), eq(Artist.class));
        verify(artistRepository, times(1)).save(mappedArtist);
    }

    @Test
    public void testDeleteArtist() throws ArtistNotFoundException {
        long id = 3;

        Artist artistToDelete = mockArtists.getLast();

        when(artistRepository.findById(id)).thenReturn(Optional.of(mockArtists.getLast()));
        doNothing().when(artistRepository).delete(artistToDelete);

        Artist result = artistService.getById(id);

        assertEquals(artistToDelete, result);

        artistService.delete(id);

        verify(artistRepository, times(1)).delete(artistToDelete);
    }

    @Test
    public void testModifyArtist() throws ArtistNotFoundException {
        long id = 1;

        Artist artistToModify = mockArtists.getLast();

        ArtistInDto updatedData = new ArtistInDto("Olly Alexander", true,
                "new_image.jpg", "United Kingdom");

        Artist updatedArtist = new Artist(
                3, "Olly Alexander", LocalDate.parse("2021-07-03"),
                true, "new_image.jpg", "United Kingdom",null
        );

        when(artistRepository.findById(id)).thenReturn(Optional.of(artistToModify));

        doAnswer(invocation -> {
            ArtistInDto dto = invocation.getArgument(0);
            Artist target = invocation.getArgument(1);
            target.setName(dto.getName());
            target.setSoloist(dto.isSoloist());
            target.setImage(dto.getImage());
            target.setCountry(dto.getCountry());
            return null;
        }).when(modelMapper).map(updatedData, artistToModify);

        when(artistRepository.save(artistToModify)).thenReturn(updatedArtist);

        Artist result = artistService.modify(id,updatedData);

        assertEquals("Olly Alexander", result.getName());
        assertEquals("new_image.jpg", result.getImage());
        assertEquals("United Kingdom", result.getCountry());
        assertTrue(result.isSoloist());

        verify(artistRepository, times(1)).findById(id);
        verify(modelMapper).map(eq(updatedData), eq(artistToModify));
        verify(artistRepository, times(1)).save(artistToModify);
    }

    @Test
    public void testArtistNotFoundException() throws ArtistNotFoundException {
        long id = 79;

        when(artistRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ArtistNotFoundException.class, () -> {
            artistService.getById(id);
        });
    }
}
