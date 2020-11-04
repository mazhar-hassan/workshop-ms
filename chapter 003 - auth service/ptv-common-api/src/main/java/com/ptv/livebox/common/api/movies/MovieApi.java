package com.ptv.livebox.common.api.movies;

import com.github.fge.jsonpatch.JsonPatch;
import com.ptv.livebox.common.api.movies.dtos.Movie;
import com.ptv.livebox.common.api.movies.dtos.MovieDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("ptv-movies")
@RequestMapping("/api/movies")
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
