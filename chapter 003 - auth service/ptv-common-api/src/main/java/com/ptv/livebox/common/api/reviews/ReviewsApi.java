package com.ptv.livebox.common.api.reviews;


import com.ptv.livebox.common.api.reviews.dtos.Review;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("ptv-reviews")
@RequestMapping("/api")
public interface ReviewsApi {
    @GetMapping("/reviews")
    List<Review> list();

    @GetMapping("/reviews/{id}")
    Review findById(@PathVariable("id") Integer id);

    @GetMapping("/movies/{movieId}/reviews")
    List<Review> findReviewsByMovieId(@PathVariable("movieId") Integer movieId);

    @GetMapping("/running-port")
    String getPort();
}
