package com.workshop.ms.controllers;

import com.ptv.livebox.common.api.reviews.ReviewsApi;
import com.ptv.livebox.common.api.reviews.dtos.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReviewsController implements ReviewsApi {

    @Autowired
    Environment environment;

    private static List<Review> movies = new ArrayList<>();

    static {
        movies.add(new Review(1, 1, 4, "Great Movie"));
        movies.add(new Review(2, 1, 5, "Nice Alien movie"));
        movies.add(new Review(3, 3, 5, "Mad Max Fury great movie"));
        movies.add(new Review(4, 4, 3, "Optimus prime is back"));
        movies.add(new Review(5, 5, 4, "Best Sniper movie"));
        movies.add(new Review(6, 6, 3, "Tom Cruise was great"));
    }

    @Override
    public List<Review> list() {
        return movies;
    }

    @Override
    public Review findById(Integer id) {
        return movies.stream()
                .filter(review -> review.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public List<Review> findReviewsByMovieId(Integer movieId) {
        return movies.stream()
                .filter(review -> review.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }

    @Override
    public String getPort() {
        return "Random port: " + environment.getProperty("local.server.port");
    }

}
