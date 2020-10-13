package com.ptv.livebox.movie.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.ptv.livebox.movie.api.MovieApi;
import com.ptv.livebox.movie.dto.MovieDetail;
import com.ptv.livebox.movie.dto.Movie;
import com.ptv.livebox.movie.service.MovieService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController implements MovieApi {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public Movie findById(Integer id) {
        return movieService.findById(id);
    }

    @Override
    public Movie create(MovieDetail movie) {
        return movieService.create(movie);
    }

    @Override
    public Movie edit(Integer id, MovieDetail movie) {
        return movieService.edit(id, movie);
    }

    @Override
    public Movie patch(Integer id, JsonPatch patch) {
        return movieService.patch(id, patch);
    }

    @Override
    public void delete(Integer id) {
        movieService.delete(id);
    }

    @Override
    public List<Movie> list() {
        return movieService.list();
    }
}
