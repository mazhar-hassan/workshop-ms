package com.ptv.livebox.movie.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.ptv.livebox.movie.dto.MovieDetail;
import com.ptv.livebox.movie.dto.Movie;

import java.util.List;

public interface MovieService {
    Movie findById(Integer id);

    Movie create(MovieDetail movie);

    Movie edit(Integer id, MovieDetail movie);

    void delete(Integer id);

    List<Movie> list();

    Movie patch(Integer id, JsonPatch patch);
}
