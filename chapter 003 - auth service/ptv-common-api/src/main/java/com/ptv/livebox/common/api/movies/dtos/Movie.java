package com.ptv.livebox.common.api.movies.dtos;

import com.ptv.livebox.common.api.reviews.dtos.Review;

import java.util.List;

public class Movie extends CreateMovie {
    private Integer id;
    private List<Review> reviews;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
