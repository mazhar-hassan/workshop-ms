package com.ptv.livebox.movie.mapper;

import com.ptv.livebox.movie.dao.entity.MovieEntity;
import com.ptv.livebox.movie.dto.MovieDetail;
import com.ptv.livebox.movie.dto.Movie;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie map(MovieEntity entity);

    MovieEntity map(Movie movie);

    MovieEntity map(MovieDetail movie);

    List<Movie> map(List<MovieEntity> entities);
}
