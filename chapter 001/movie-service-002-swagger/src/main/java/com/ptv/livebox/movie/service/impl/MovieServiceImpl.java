package com.ptv.livebox.movie.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.ptv.livebox.movie.dao.MovieRepository;
import com.ptv.livebox.movie.dao.entity.MovieEntity;
import com.ptv.livebox.movie.dto.Movie;
import com.ptv.livebox.movie.mapper.MovieMapper;
import com.ptv.livebox.movie.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }


    @Override
    public Movie findById(Integer id) {

        return movieMapper.map(movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found")));
    }

    @Override
    public Movie create(Movie movie) {
        MovieEntity entity = movieMapper.map(movie);
        movieRepository.save(entity);

        return movieMapper.map(entity);
    }

    @Override
    public Movie edit(Movie movie) {
        MovieEntity entity = movieMapper.map(movie);
        movieRepository.save(entity);

        return movieMapper.map(entity);
    }

    @Override
    public void delete(Integer id) {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        movieRepository.delete(movie);
    }

    @Override
    public List<Movie> list() {
        List<MovieEntity> list = movieRepository.findAll();

        return movieMapper.map(list);
    }

    @Override
    public Movie patch(Integer id, JsonPatch patch) {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        MovieEntity patched = null;
        try {
            patched = applyPatch(patch, movie);
            movieRepository.save(patched);
        } catch (JsonPatchException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return movieMapper.map(patched);
    }

    private MovieEntity applyPatch(JsonPatch patch, MovieEntity movie) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(movie, JsonNode.class));

        return objectMapper.treeToValue(patched, MovieEntity.class);
    }
}
