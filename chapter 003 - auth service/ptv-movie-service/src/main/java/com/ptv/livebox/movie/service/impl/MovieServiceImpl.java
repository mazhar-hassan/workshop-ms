package com.ptv.livebox.movie.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.o4.microservices.common.exceptions.MissingRequiredFieldException;
import com.o4.microservices.common.exceptions.RecordNotFoundException;
import com.ptv.livebox.common.api.movies.dtos.Movie;
import com.ptv.livebox.common.api.movies.dtos.MovieDetail;
import com.ptv.livebox.common.api.reviews.ReviewsApi;
import com.ptv.livebox.movie.dao.MovieRepository;
import com.ptv.livebox.movie.dao.MovieSpecifications;
import com.ptv.livebox.movie.dao.entity.MovieEntity;
import com.ptv.livebox.movie.dto.MovieSearchRequest;
import com.ptv.livebox.movie.mapper.MovieMapper;
import com.ptv.livebox.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    ReviewsApi reviewsApi;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public Movie findById(Integer id) {
        Movie movie = movieMapper.map(fetchMovie(id));
        movie.setReviews(reviewsApi.findReviewsByMovieId(id));
        return movie;
    }

    @Override
    public Movie create(MovieDetail movie) {
        validate(movie);
        MovieEntity entity = movieMapper.map(movie);
        movieRepository.save(entity);

        return movieMapper.map(entity);
    }

    @Override
    public Movie edit(Integer id, MovieDetail movie) {
        validate(movie);
        MovieEntity existing = fetchMovie(id);
        movieMapper.mapOnTo(movie, existing);
        movieRepository.save(existing);

        return movieMapper.map(existing);
    }

    @Override
    public void delete(Integer id) {

        movieRepository.delete(fetchMovie(id));
    }

    @Override
    public List<Movie> list() {
        List<MovieEntity> list = movieRepository.findAll();

        return movieMapper.map(list);
    }

    @Override
    public List<Movie> search(MovieSearchRequest request) {
        Page<MovieEntity> result = movieRepository.findAll(new MovieSpecifications(request),
                PageRequest.of(0, request.getMaxRows(), defaultSort()));

        return movieMapper.map(result.getContent());
    }

    private Sort defaultSort() {
        return Sort.by(Sort.Order.asc("title"), Sort.Order.desc("genera"));
    }

    @Override
    public Movie patch(Integer id, JsonPatch patch) {
        MovieEntity movie = fetchMovie(id);

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

    private MovieEntity fetchMovie(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Movie not found"));
    }

    private void validate(MovieDetail movie) {
        if (movie == null) {
            throw new MissingRequiredFieldException("Required movie object is null");
        } else if (movie.getTitle() == null || movie.getTitle().isEmpty()) {
            throw new MissingRequiredFieldException("Movie title is missing");
        } else if (movie.getGenera() == null) {
            throw new MissingRequiredFieldException("Movie genera is missing");
        }
    }
}
