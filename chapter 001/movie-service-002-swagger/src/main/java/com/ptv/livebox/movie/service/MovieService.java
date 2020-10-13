package com.ptv.livebox.movie.service;

import com.ptv.livebox.movie.dto.Movie;

import java.util.List;

public interface MovieService {
    Movie findById(Integer id);

    Movie create(Movie movie);

    Movie edit(Movie movie);

    void delete(Integer id);

    List<Movie> list();
}
