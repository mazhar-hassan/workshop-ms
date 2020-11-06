package com.ptv.livebox.movie.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptv.livebox.common.api.reviews.ReviewsApi;
import com.ptv.livebox.common.api.reviews.dtos.Review;
import com.ptv.livebox.movie.dto.MovieSearchRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
    public void testFindById() throws Exception {

        Mockito.when(reviewsApi.findReviewsByMovieId(1)).thenReturn(getMockingReviews());

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/api/movies/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void testSearchMovieByTitle() throws Exception {

        String json = mapper.writeValueAsString(createByTitleSearch());

        mockMvc.perform(post("/api/movies/search")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Test 1 Movie"));

    }

    private MovieSearchRequest createByTitleSearch() {
        MovieSearchRequest request = new MovieSearchRequest();
        request.setTitle("Test 1");

        return request;
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
