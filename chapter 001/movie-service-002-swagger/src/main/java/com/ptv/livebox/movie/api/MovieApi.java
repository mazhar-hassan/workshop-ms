package com.ptv.livebox.movie.api;

import com.github.fge.jsonpatch.JsonPatch;
import com.ptv.livebox.movie.dto.MovieDetail;
import com.ptv.livebox.movie.dto.Movie;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface MovieApi {

    @GetMapping("/{id}")
    Movie findById(@PathVariable("id") Integer id);

    @PostMapping
    Movie create(@RequestBody MovieDetail movie);

    @PutMapping("/{id}")
    Movie edit(@PathVariable("id") Integer id, @RequestBody MovieDetail movie);

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    Movie patch(@PathVariable("id") Integer id, @RequestBody JsonPatch patch);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Integer id);

    @GetMapping
    List<Movie> list();


}
