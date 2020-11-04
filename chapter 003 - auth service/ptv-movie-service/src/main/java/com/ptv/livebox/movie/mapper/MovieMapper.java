package com.ptv.livebox.movie.mapper;

import com.ptv.livebox.common.api.movies.dtos.Movie;
import com.ptv.livebox.common.api.movies.dtos.MovieDetail;
import com.ptv.livebox.movie.dao.entity.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie map(MovieEntity entity);

    MovieEntity map(Movie movie);

    MovieEntity map(MovieDetail movie);

    void mapOnTo(MovieDetail movie, @MappingTarget MovieEntity entity);

    List<Movie> map(List<MovieEntity> entities);
}
