package com.svalero.musicApi;

import com.svalero.musicApi.domain.Artist;
import com.svalero.musicApi.domain.ArtistInDto;
import com.svalero.musicApi.exception.ArtistNotFoundException;
import com.svalero.musicApi.security.JwtUtil;
import com.svalero.musicApi.service.ArtistService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

    private String token;

    private final List<Artist> mockArtists = List.of(
            new Artist(1, "Roxette", LocalDate.parse("2005-02-15"), false, null, "Sweden", null),
            new Artist(2, "Beast in black", LocalDate.parse("2015-04-28"), false, null, "Finland", null),
            new Artist(3, "Olly Alexander", LocalDate.parse("2021-07-03"), true, null, "UK",null)
    );

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil();
        this.token = "Bearer " + jwtUtil.generateToken("demo@example.com");
    }

    //Response HTTP 200
    @Test
    void getAllArtists_ShouldReturnOK() throws Exception {

        when(artistService.getAll()).thenReturn(mockArtists);

        mockMvc.perform(get("/api/v1/artists")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Roxette")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Olly Alexander")));

        verify(artistService).getAll();
    }

    //Response HTTP 401 Unauthorized
    @Test
    void getAllArtists_WhenNotLogged_ShouldReturnKO() throws Exception {

        when(artistService.getAll()).thenReturn(mockArtists);

        mockMvc.perform(get("/api/v1/artists"))
                .andExpect(status().isUnauthorized());

        verify(artistService, never()).getAll();
    }

    //Response HTTP 200
    @Test
    void getArtistById_WhenExists_ShouldReturnOK() throws Exception {
        when(artistService.getById(1)).thenReturn(mockArtists.getFirst());

        mockMvc.perform(get("/api/v1/artists/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Roxette")))
                .andExpect(jsonPath("$.country", is("Sweden")));

        verify(artistService).getById(1);
    }

    //Response HTTP 404 Not Found
    @Test
    void getArtistById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        long id = 79;
        when(artistService.getById(id)).thenThrow(new ArtistNotFoundException());

        mockMvc.perform(get("/api/v1/artists/" + id)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Artist not found")));

        verify(artistService).getById(id);
    }

    //Response HTTP 401 Unauthorized
    @Test
    void getArtistById_WhenNotLogged_ShouldReturnKO() throws Exception {

        when(artistService.getById(1)).thenReturn(mockArtists.getFirst());

        mockMvc.perform(get("/api/v1/artists/1"))
                .andExpect(status().isUnauthorized());

        verify(artistService, never()).getAll();
    }

    //Response HTTP 201 Created
    @Test
    void addArtist_ShouldReturnOK() throws Exception {

        Artist newArtist = new Artist(
                4, "Within Temptation", LocalDate.now()
                ,false, null, "Denmark", null
        );

        String requestBody = """
                {
                    "name": "Within Temptation",
                    "image": null,
                    "country": "Denmark",
                    "soloist": false
                }
                """;

        when(artistService.add(any(ArtistInDto.class))).thenReturn(newArtist);

        mockMvc.perform(post("/api/v1/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("Within Temptation")))
                .andExpect(jsonPath("$.soloist", is(false)));

        verify(artistService).add(any(ArtistInDto.class));
    }

    //Response HTTP 400 Bad request
    @Test
    void addArtist_BadRequest_ShouldReturnKO() throws Exception {

        String invalidRequestBody = """
                {
                    "image": null,
                    "country": "Denmark",
                    "soloist": false
                }
                """;

        mockMvc.perform(post("/api/v1/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessages").exists())
                .andExpect(jsonPath("$.errorMessages.name").exists());

        verify(artistService, never()).add(any(ArtistInDto.class));
    }

    //Response HTTP 401 Unauthorized
    @Test
    void addArtist_WhenNotLogged_ShouldReturnKO() throws Exception {

        mockMvc.perform(post("/api/v1/artists/1"))
                .andExpect(status().isUnauthorized());

        verify(artistService, never()).add(any(ArtistInDto.class));
    }

    //Response HTTP 204 No content
    @Test
    void deleteArtist_WhenExistsAndLogged_ShouldReturnOK() throws Exception {
        mockMvc.perform(delete("/api/v1/artists/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNoContent());

        verify(artistService).delete(1);
    }

    //Response HTTP 401 Unauthorized
    @Test
    void deleteArtist_WhenNotLogged_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/artists/1"))
                .andExpect(status().isUnauthorized());

        verify(artistService, never()).delete(1);
    }

    //Response HTTP 404 Not Found
    @Test
    void deleteArtist_WhenNotExistsAndLogged_ShouldReturnNotFound() throws Exception {
        long id = 79;
        doThrow(new ArtistNotFoundException()).when(artistService).delete(id);
        mockMvc.perform(delete("/api/v1/artists/" + id)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Artist not found")));

        verify(artistService).delete(id);
    }

    //Response HTTP 200
    @Test
    void modifyArtist_WhenExists_ShouldReturnOK() throws Exception {
        long id = 1;
        Artist modifiedArtist = new Artist(
                1, "Roxette", LocalDate.parse("2005-02-15"),
                false, "new_image.jpg", "Sweden", null
        );

        when(artistService.modify(eq(id), any(ArtistInDto.class))).thenReturn(modifiedArtist);

        String requestBody = """
                {
                    "name": "Roxette",
                    "image": "new_image.jpg",
                    "country": "Sweden",
                    "soloist": false
                }
                """;

        mockMvc.perform(put("/api/v1/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Roxette")))
                .andExpect(jsonPath("$.image", is("new_image.jpg")));

        verify(artistService).modify(eq(id), any(ArtistInDto.class));
    }

    //Response HTTP 404 Not Found
    @Test
    void modifyArtist_WhenNotExists_ShouldReturnKO() throws Exception {
        long id = 79;

        when(artistService.modify(eq(id), any(ArtistInDto.class)))
                .thenThrow(new ArtistNotFoundException());

        String requestBody = """
                {
                    "name": "Roxette",
                    "image": "new_image.jpg",
                    "country": "Sweden",
                    "soloist": false
                }
                """;

        mockMvc.perform(put("/api/v1/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Artist not found")));

        verify(artistService).modify(eq(id), any(ArtistInDto.class));
    }

    //Response HTTP 400 Bad Request
    @Test
    void modifyArtist_BadRequest_ShouldReturnKO() throws Exception {
        long id = 1;

        String invalidRequestBody = """
                {
                     "image": "new_image.jpg",
                     "country": "Sweden",
                     "soloist": false
                }
                """;

        mockMvc.perform(put("/api/v1/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessages").exists())
                .andExpect(jsonPath("$.errorMessages.name").exists());

        verify(artistService, never()).modify(eq(id), any(ArtistInDto.class));
    }

    //Response HTTP 401 Unauthorized
    @Test
    void modifyArtist_WhenNotLogged_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(put("/api/v1/artists/1"))
                .andExpect(status().isUnauthorized());

        verify(artistService, never()).delete(1);
    }
}
