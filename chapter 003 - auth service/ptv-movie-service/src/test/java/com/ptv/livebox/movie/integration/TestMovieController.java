package com.ptv.livebox.movie.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.o4.microservices.common.exceptions.RecordNotFoundException;
import com.ptv.livebox.common.api.movies.dtos.CreateMovie;
import com.ptv.livebox.common.api.movies.dtos.Movie;
import com.ptv.livebox.common.api.movies.dtos.MovieGenera;
import com.ptv.livebox.common.api.movies.dtos.Publisher;
import com.ptv.livebox.common.api.reviews.ReviewsApi;
import com.ptv.livebox.common.api.reviews.dtos.Review;
import com.ptv.livebox.movie.common.utils.DateTimeUtils;
import com.ptv.livebox.movie.dao.MovieGeneraRepository;
import com.ptv.livebox.movie.dao.PublisherRepository;
import com.ptv.livebox.movie.dao.entity.MovieGeneraEntity;
import com.ptv.livebox.movie.dao.entity.PublisherEntity;
import com.ptv.livebox.movie.dto.MovieSearchRequest;
import com.ptv.livebox.security.common.data.AuthenticatedUser;
import com.ptv.livebox.security.common.data.AutheticatedUserRole;
import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestMovieController {

    @MockBean
    ReviewsApi reviewsApi;

    @Inject
    public MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    @Autowired
    MovieGeneraRepository generaRepository;

    @Autowired
    PublisherRepository publisherRepository;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSearchMovieByTitle() throws Exception {

        String json = mapper.writeValueAsString(createByTitleSearch());

        mockMvc.perform(secure(post("/api/movies/search"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Rambo II (JU)"));

    }

    @Test
    public void testCreate() throws Exception {
        Movie responseMovie = createMovie("Junit Movie");
        assertNotNull(responseMovie);

        //verify if response is actually saved in database
        Movie dbMovie = fetchById(responseMovie.getId());
        assertEquals(responseMovie.getDescription(), dbMovie.getDescription());
        assertEquals(responseMovie.getPublisher().getPublisher(), dbMovie.getPublisher().getPublisher());
        assertEquals(responseMovie.getPublisher().getCountry(), dbMovie.getPublisher().getCountry());
        //date comparison
        assertThat(responseMovie.getPublisher().getPublishDate().equals(dbMovie.getPublisher().getPublishDate())).isTrue();

        List<MovieGenera> generas = List.of(MovieGenera.ADVENTURE, MovieGenera.ACTION);
        assertTrue(!Collections.isEmpty(dbMovie.getGeneras()));
        assertThat(generas).containsAll(dbMovie.getGeneras());
    }

    @Test
    public void testUpdate() throws Exception {
        Movie dto = createMovie("Junit Movie to Update");
        dto.setTitle("Updated: Movie JUNIT");

        dto.setGeneras(List.of(MovieGenera.WAR, MovieGenera.DOCUMENTARY, MovieGenera.ACTION));
        dto.getPublisher().setCountry("UE");

        MvcResult response = mockMvc.perform(secure(put("/api/movies/" + dto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andReturn();

        Movie responseMovie = mapper.readValue(response.getResponse().getContentAsString(), Movie.class);
        assertEquals(dto.getPublisher().getCountry(), responseMovie.getPublisher().getCountry());

        assertThat(dto.getGeneras()).containsAll(responseMovie.getGeneras());

        //verify if response is actually saved in database
        Movie dbMovie = fetchById(responseMovie.getId());
        assertEquals(responseMovie.getDescription(), dbMovie.getDescription());
        assertEquals(responseMovie.getPublisher().getPublisher(), dbMovie.getPublisher().getPublisher());
        assertEquals(responseMovie.getPublisher().getCountry(), dbMovie.getPublisher().getCountry());
        //date comparison
        assertThat(responseMovie.getPublisher().getPublishDate().equals(dbMovie.getPublisher().getPublishDate())).isTrue();

        assertTrue(!Collections.isEmpty(dbMovie.getGeneras()));
        assertThat(dto.getGeneras()).containsAll(dbMovie.getGeneras());

    }

    @Test
    public void testDelete() throws Exception {
        Movie dto = createMovie("Junit Movie to Delete");

        MvcResult response = mockMvc.perform(secure(delete("/api/movies/" + dto.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //verify if item is deleted
        fetchById404(dto.getId());

        //verify no generas
        assertTrue(Collections.isEmpty(generaRepository.findAllByMovie_Id(dto.getId())));

        //verify no Publisher
        assertNull(publisherRepository.findByMovie_Id(dto.getId()));
    }

    private Movie createMovie(String title) throws Exception {
        CreateMovie dto = createMovieDTO(title);
        String json = mapper.writeValueAsString(dto);

        MvcResult response = mockMvc.perform(secure(secure(post("/api/movies")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andReturn();

        System.out.println(response.getResponse().getContentAsString());
        Movie movie = mapper.readValue(response.getResponse().getContentAsString(), Movie.class);

        assertEquals(dto.getDescription(), movie.getDescription());
        assertEquals(dto.getPublisher().getPublisher(), movie.getPublisher().getPublisher());
        assertEquals(dto.getPublisher().getCountry(), movie.getPublisher().getCountry());
        //date comparison
        assertThat(dto.getPublisher().getPublishDate().equals(movie.getPublisher().getPublishDate())).isTrue();

        List<MovieGenera> generas = List.of(MovieGenera.ADVENTURE, MovieGenera.ACTION);
        assertThat(generas).containsAll(movie.getGeneras());

        return movie;
    }

    private MockHttpServletRequestBuilder secure(MockHttpServletRequestBuilder builder) {
        try {
            return builder.header("user", mapper.writeValueAsString(createSecurityToken()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception occurred while conversion");
        }
    }

    @Test
    public void testFindById() throws Exception {

        Mockito.when(reviewsApi.findReviewsByMovieId(1)).thenReturn(getMockingReviews());

        MvcResult response = mockMvc.perform(secure(get("/api/movies/1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(response.getResponse().getContentAsString());
        Movie movie = mapper.readValue(response.getResponse().getContentAsString(), Movie.class);
        assertEquals("Terminator is all about bots to take over human race", movie.getDescription());
        assertEquals("Pub1", movie.getPublisher().getPublisher());
        assertEquals("US", movie.getPublisher().getCountry());
        assertEquals(DateTimeUtils.parse("2020-11-11T10:19:11.000+00:00"),
                movie.getPublisher()
                        .getPublishDate());
        List<MovieGenera> generas = List.of(MovieGenera.ADVENTURE, MovieGenera.ACTION);
        assertThat(generas).containsAll(movie.getGeneras());

    }

    private void fetchById404(Integer id) throws Exception {
        fetchByIdMvcResult(id, status().is(404));
    }

    private MvcResult fetchByIdMvcResult(Integer id, ResultMatcher statusMatacher) throws Exception {
        Mockito.when(reviewsApi.findReviewsByMovieId(id)).thenReturn(getMockingReviews());

        return mockMvc.perform(secure(get("/api/movies/" + id)))
                .andDo(print())
                .andExpect(statusMatacher)
                .andReturn();
    }

    private Movie fetchById(Integer id) throws Exception {
        MvcResult response = fetchByIdMvcResult(id, status().isOk());

        return mapper.readValue(response.getResponse().getContentAsString(), Movie.class);
    }

    private AuthenticatedUser createSecurityToken() {
        AutheticatedUserRole role1 = new AutheticatedUserRole();
        role1.setId(1);
        role1.setRoleName("Junit Role");

        AuthenticatedUser user = new AuthenticatedUser();
        user.setId(420);
        user.setUsername("Some Junit User");
        user.setRoles(new ArrayList<>());
        user.getRoles().add(role1);

        return user;
    }

    private MovieSearchRequest createByTitleSearch() {
        MovieSearchRequest request = new MovieSearchRequest();
        request.setTitle("Rambo");

        return request;
    }

    private CreateMovie createMovieDTO(String title) {
        Publisher publisher = new Publisher();
        publisher.setPublishDate(new Date());
        publisher.setCountry("PK");
        publisher.setPublisher("Here is publisher");

        CreateMovie movie = new CreateMovie();
        movie.setTitle(title);
        movie.setDescription("Here is description");
        movie.setPublisher(publisher);

        movie.setGeneras(new ArrayList<>());
        movie.getGeneras().add(MovieGenera.ADVENTURE);
        movie.getGeneras().add(MovieGenera.ACTION);

        return movie;
    }

    private List<Review> getMockingReviews() {
        List<Review> reviews = new ArrayList<>();
        Review review = new Review();
        review.setId(1);
        review.setMessage("Test Message");
        review.setMovieId(1);
        review.setRating(3);
        reviews.add(review);

        return reviews;
    }

}
