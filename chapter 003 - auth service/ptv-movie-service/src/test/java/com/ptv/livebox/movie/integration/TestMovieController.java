package com.ptv.livebox.movie.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptv.livebox.common.api.movies.dtos.CreateMovie;
import com.ptv.livebox.common.api.movies.dtos.Movie;
import com.ptv.livebox.common.api.movies.dtos.MovieGenera;
import com.ptv.livebox.common.api.movies.dtos.Publisher;
import com.ptv.livebox.common.api.reviews.ReviewsApi;
import com.ptv.livebox.common.api.reviews.dtos.Review;
import com.ptv.livebox.movie.common.utils.DateTimeUtils;
import com.ptv.livebox.movie.dto.MovieSearchRequest;
import com.ptv.livebox.security.common.data.AuthenticatedUser;
import com.ptv.livebox.security.common.data.AutheticatedUserRole;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        CreateMovie dto = createMovieDTO();
        String json = mapper.writeValueAsString(createMovieDTO());

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

    private CreateMovie createMovieDTO() {
        Publisher publisher = new Publisher();
        publisher.setPublishDate(new Date());
        publisher.setCountry("PK");
        publisher.setPublisher("Here is publisher");

        CreateMovie movie = new CreateMovie();
        movie.setTitle("Junit Movie");
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
