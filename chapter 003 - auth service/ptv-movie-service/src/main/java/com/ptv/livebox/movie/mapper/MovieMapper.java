package com.ptv.livebox.movie.mapper;

import com.ptv.livebox.common.api.movies.dtos.CreateMovie;
import com.ptv.livebox.common.api.movies.dtos.Movie;
import com.ptv.livebox.common.api.movies.dtos.MovieGenera;
import com.ptv.livebox.common.api.movies.dtos.Publisher;
import com.ptv.livebox.movie.dao.entity.MovieEntity;
import com.ptv.livebox.movie.dao.entity.MovieGeneraEntity;
import com.ptv.livebox.movie.dao.entity.PublisherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie map(MovieEntity entity);

    MovieEntity map(Movie movie);

    MovieEntity map(CreateMovie movie);

    void mapOnTo(CreateMovie movie, @MappingTarget MovieEntity entity);

    List<Movie> map(List<MovieEntity> entities);

    Publisher map(PublisherEntity entity);

    PublisherEntity map(Publisher dto);

    List<MovieGenera> toGeneraDTOList(List<MovieGeneraEntity> generaList);

    default MovieGenera toGeneraDTO(MovieGeneraEntity genera) {
        return genera.getGenera();
    }

    List<MovieGeneraEntity> toGeneraEntities(List<MovieGenera> generaList);

    default MovieGeneraEntity toGeneraEntity(MovieGenera genera) {
        MovieGeneraEntity entity = new MovieGeneraEntity();
        entity.setGenera(genera);
        return entity;
    }
}
