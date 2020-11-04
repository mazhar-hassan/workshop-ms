package com.ptv.livebox.common.api.reviews.dtos;

public class Review {
    private Integer id;
    private Integer movieId;
    private Integer rating;
    private String message;

    public Review() {

    }

    public Review(Integer id, Integer movieId, Integer rating, String message) {
        this.id = id;
        this.movieId = movieId;
        this.rating = rating;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
