package com.ptv.livebox.movie.unit;

import com.ptv.livebox.common.api.movies.dtos.Movie;
import com.ptv.livebox.common.api.movies.dtos.MovieGenera;
import com.ptv.livebox.movie.dao.entity.MovieEntity;
import com.ptv.livebox.movie.dao.entity.MovieGeneraEntity;
import com.ptv.livebox.movie.mapper.MovieMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieMapperTest {


    MovieMapper mapper = Mappers.getMapper(MovieMapper.class);

    @Test
    public void testDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        OffsetDateTime date = OffsetDateTime.parse("2020-11-11T10:19:11.000+00:00");
        Date dd = new Date(date.toInstant().toEpochMilli());

        System.out.println(dd);

        System.out.println(dateFormat.parse("2020-11-11T10:19:11.000+00:00"));

    }

    @Test
    public void testMapEntityToDto() {

        MovieGeneraEntity generaEntity = new MovieGeneraEntity();
        generaEntity.setGenera(MovieGenera.ADVENTURE);

        MovieGeneraEntity movieGeneraEntity = new MovieGeneraEntity();
        movieGeneraEntity.setGenera(MovieGenera.ADVENTURE);

        MovieEntity movie = new MovieEntity();
        movie.setId(1);
        movie.setDescription("This is description");
        movie.setGeneras(List.of(movieGeneraEntity));
        movie.setTitle("Junit Movie");
        Movie object = mapper.map(movie);

        assertEquals(movie.getTitle(), object.getTitle());
        assertEquals(movie.getId(), object.getId());
        List<MovieGenera> generas = movie.getGeneras().stream()
                .map(g -> g.getGenera())
                .collect(Collectors.toList());

        assertThat(object.getGeneras()).containsAll(generas);
        assertEquals(movie.getDescription(), object.getDescription());
    }

    @Test
    public void testMapDtoToEntity() {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setDescription("This is description");
        movie.setGeneras(List.of(MovieGenera.ADVENTURE));
        movie.setTitle("Junit Movie");

        MovieEntity object = mapper.map(movie);

        assertEquals(movie.getTitle(), object.getTitle());
        assertEquals(movie.getId(), object.getId());

        List<MovieGenera> generas = object.getGeneras().stream()
                .map(g -> g.getGenera())
                .collect(Collectors.toList());

        assertThat(movie.getGeneras()).containsAll(generas);
        assertEquals(movie.getDescription(), object.getDescription());
    }
}
