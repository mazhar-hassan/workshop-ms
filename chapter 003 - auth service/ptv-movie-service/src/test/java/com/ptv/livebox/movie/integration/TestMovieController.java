package com.ptv.livebox.movie.integration;

import com.ptv.livebox.common.api.reviews.ReviewsApi;
import com.ptv.livebox.common.api.reviews.dtos.Review;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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


    @Test
    public void testFindById() throws Exception {

        Mockito.when(reviewsApi.findReviewsByMovieId(1)).thenReturn(getMockingReviews());

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/api/movies/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

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
