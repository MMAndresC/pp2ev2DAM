package com.svalero.musicApi;

import com.svalero.musicApi.domain.*;
import com.svalero.musicApi.exception.AlbumNotFoundException;
import com.svalero.musicApi.security.JwtUtil;
import com.svalero.musicApi.service.AlbumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    private String token;

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

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil();
        this.token = "Bearer " + jwtUtil.generateToken("demo@example.com");
    }

    //Response HTTP 200
    @Test
    void getAllAlbums_ShouldReturnOK() throws Exception {

        when(albumService.getAll()).thenReturn(mockAlbums);

        mockMvc.perform(get("/api/v1/albums")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Joyride")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].title", is("From hell with love")));

        verify(albumService).getAll();
    }

    //Response HTTP 401 Unauthorized
    @Test
    void getAllAlbums_WhenNotLogged_ShouldReturnKO() throws Exception {

        when(albumService.getAll()).thenReturn(mockAlbums);

        mockMvc.perform(get("/api/v1/albums"))
                .andExpect(status().isUnauthorized());

        verify(albumService, never()).getAll();
    }

    //Response HTTP 200
    @Test
    void getAlbumById_WhenExists_ShouldReturnOK() throws Exception {
        when(albumService.getById(1)).thenReturn(mockAlbums.getFirst());

        mockMvc.perform(get("/api/v1/albums/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Joyride")))
                .andExpect(jsonPath("$.tracks", is(15)));

        verify(albumService).getById(1);
    }

    //Response HTTP 404 Not Found
    @Test
    void getAlbumById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        long id = 79;
        when(albumService.getById(id)).thenThrow(new AlbumNotFoundException());

        mockMvc.perform(get("/api/v1/albums/" + id)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Album not found")));

        verify(albumService).getById(id);
    }

    //Response HTTP 401 Unauthorized
    @Test
    void getAlbumById_WhenNotLogged_ShouldReturnKO() throws Exception {

        when(albumService.getById(1)).thenReturn(mockAlbums.getFirst());

        mockMvc.perform(get("/api/v1/albums/1"))
                .andExpect(status().isUnauthorized());

        verify(albumService, never()).getAll();
    }

    //Response HTTP 201 Created
    @Test
    void addAlbum_ShouldReturnOK() throws Exception {
        long artistId = 2;
        Album newAlbum = new Album(
                5,"Berserker", LocalDate.parse("2017-11-03"),
                null, "metal", 12, 51.09,
                "Nuclear Blast", false, mockArtists.get(1)
        );

        String requestBody = """
                {
                     "title": "Berserker",
                     "releaseDate": "2017-11-03",
                     "front": null,
                     "genre": "metal",
                     "tracks": 12,
                     "duration": 51.09,
                     "label": "Nuclear Blast",
                     "platinum": false
                }
                """;

        when(albumService.add(any(AlbumInDto.class),eq(artistId))).thenReturn(newAlbum);

        mockMvc.perform(post("/api/v1/artists/" + artistId + "/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.title", is("Berserker")))
                .andExpect(jsonPath("$.genre", is("metal")))
                .andExpect(jsonPath("$.platinum", is(false)));

        verify(albumService).add(any(AlbumInDto.class), eq(artistId));
    }

    //Response HTTP 400 Bad request
    @Test
    void addAlbum_BadRequest_ShouldReturnKO() throws Exception {
        long artistId = 2;
        String invalidRequestBody = """
               {
                     "releaseDate": "2017-11-03",
                     "front": null,
                     "genre": "metal",
                     "tracks": 12,
                     "duration": 51.09,
                     "label": "Nuclear Blast",
                     "platinum": false
                }
                """;

        mockMvc.perform(post("/api/v1/artists/" + artistId + "/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessages").exists())
                .andExpect(jsonPath("$.errorMessages.title").exists());

        verify(albumService, never()).add(any(AlbumInDto.class), eq(artistId));
    }

    //Response HTTP 401 Unauthorized
    @Test
    void addAlbum_WhenNotLogged_ShouldReturnKO() throws Exception {
        long artistId = 2;
        mockMvc.perform(post("/api/v1/artists/" + artistId + "/albums"))
                .andExpect(status().isUnauthorized());

        verify(albumService, never()).add(any(AlbumInDto.class), eq(artistId));
    }

    //Response HTTP 204 No content
    @Test
    void deleteAlbum_WhenExistsAndLogged_ShouldReturnOK() throws Exception {
        mockMvc.perform(delete("/api/v1/albums/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNoContent());

        verify(albumService).delete(1);
    }

    //Response HTTP 401 Unauthorized
    @Test
    void deleteAlbum_WhenNotLogged_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/albums/1"))
                .andExpect(status().isUnauthorized());

        verify(albumService, never()).delete(1);
    }

    //Response HTTP 404 Not Found
    @Test
    void deleteAlbum_WhenNotExistsAndLogged_ShouldReturnNotFound() throws Exception {
        long id = 79;
        doThrow(new AlbumNotFoundException()).when(albumService).delete(id);
        mockMvc.perform(delete("/api/v1/albums/" + id)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Album not found")));

        verify(albumService).delete(id);
    }

    //Response HTTP 200
    @Test
    void modifyAlbum_WhenExists_ShouldReturnOK() throws Exception {
        long id = 4;
        Album modifiedAlbum = new Album(
                4, "Polar", LocalDate.parse("2025-02-07"),
                "new_image.jpg", "electro-pop", 13, 40.15,
                "Polydor", true, mockArtists.getLast()
        );

        when(albumService.modify(eq(id), any(AlbumModifyDto.class))).thenReturn(modifiedAlbum);

        String requestBody = """
                {
                     "title": "Polar",
                     "releaseDate": "2025-02-07",
                     "front": "new_image.jpg",
                     "genre": "electro-pop",
                     "tracks": 13,
                     "duration": 40.15,
                     "label": "Polydor",
                     "platinum": true,
                     "idArtist": 3
                }
                """;

        mockMvc.perform(put("/api/v1/albums/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.title", is("Polar")))
                .andExpect(jsonPath("$.genre", is("electro-pop")))
                .andExpect(jsonPath("$.platinum", is(true)))
                .andExpect(jsonPath("$.front", is("new_image.jpg")));

        verify(albumService).modify(eq(id), any(AlbumModifyDto.class));
    }

    //Response HTTP 404 Not Found
    @Test
    void modifyAlbum_WhenNotExists_ShouldReturnKO() throws Exception {
        long id = 79;

        when(albumService.modify(eq(id), any(AlbumModifyDto.class)))
                .thenThrow(new AlbumNotFoundException());

        String requestBody = """
               {
                     "title": "Polar",
                     "releaseDate": "2025-02-07",
                     "front": "new_image.jpg",
                     "genre": "electro-pop",
                     "tracks": 13,
                     "duration": 40.15,
                     "label": "Polydor",
                     "platinum": true,
                     "idArtist": 3
                }
                """;

        mockMvc.perform(put("/api/v1/albums/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Album not found")));

        verify(albumService).modify(eq(id), any(AlbumModifyDto.class));
    }

    //Response HTTP 400 Bad Request
    @Test
    void modifyAlbum_BadRequest_ShouldReturnKO() throws Exception {
        long id = 4;

        String invalidRequestBody = """
                {
                   "releaseDate": "2025-02-07",
                   "front": "new_image.jpg",
                   "genre": "electro-pop",
                   "tracks": 13,
                   "duration": 40.15,
                   "label": "Polydor",
                   "platinum": true,
                   "idArtist": 3
                }
                """;

        mockMvc.perform(put("/api/v1/albums/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessages").exists())
                .andExpect(jsonPath("$.errorMessages.title").exists());

        verify(albumService, never()).modify(eq(id), any(AlbumModifyDto.class));
    }

    //Response HTTP 401 Unauthorized
    @Test
    void modifyAlbum_WhenNotLogged_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(put("/api/v1/albums/1"))
                .andExpect(status().isUnauthorized());

        verify(albumService, never()).delete(1);
    }
}
