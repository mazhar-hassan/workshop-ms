package com.ptv.livebox.movie.api;

import com.ptv.livebox.movie.dto.Movie;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface MovieApi {

    @GetMapping("/{id}")
    Movie findById(@PathVariable("id") Integer id);

    @PostMapping
    Movie create(@RequestBody Movie movie);

    @PutMapping("/{id}")
    Movie edit(@PathVariable("id") Integer id, @RequestBody Movie movie);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Integer id);

    @GetMapping
    List<Movie> list();


}
