package com.ptv.livebox.movie.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.ptv.livebox.common.api.movies.MovieApi;
import com.ptv.livebox.common.api.movies.dtos.CreateMovie;
import com.ptv.livebox.common.api.movies.dtos.Movie;
import com.ptv.livebox.common.api.movies.dtos.MovieDetail;
import com.ptv.livebox.movie.dto.MovieSearchRequest;
import com.ptv.livebox.movie.service.MovieService;
import com.ptv.livebox.security.common.token.AccessToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Movie create(CreateMovie movie) {
        return movieService.create(movie);
    }

    @Override
    public Movie edit(Integer id, CreateMovie movie) {
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

    @GetMapping("/admin")
    public String fakeAdminEndpoint(HttpServletRequest request) {


        System.out.println("*************************************************************");
        AccessToken token = (AccessToken) SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Security username: " + token.getUser().getUsername());
        System.out.println(request.getHeader("user"));

        return "Hello from Admin";
    }

    @PostMapping("/search")
    public List<Movie> search(@RequestBody MovieSearchRequest request) {
        return movieService.search(request);
    }
}
