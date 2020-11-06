package com.ptv.livebox.movie.unit;

import com.ptv.livebox.common.api.movies.dtos.Movie;
import com.ptv.livebox.common.api.movies.dtos.MovieGenera;
import com.ptv.livebox.movie.dao.entity.MovieEntity;
import com.ptv.livebox.movie.mapper.MovieMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MovieMapperTest {

    @Autowired
    MovieMapper mapper;

    @Test
    public void testMapEntityToDto() {
        MovieEntity movie = new MovieEntity();
        movie.setId(1);
        movie.setDescription("This is description");
        movie.setGenera(MovieGenera.ADVENTURE);
        movie.setTitle("Junit Movie");

        Movie object = mapper.map(movie);

        assertEquals(movie.getTitle(), object.getTitle());
        assertEquals(movie.getId(), object.getId());
        assertEquals(movie.getGenera(), object.getGenera());
        assertEquals(movie.getDescription(), object.getDescription());
    }

    @Test
    public void testMapDtoToEntity() {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setDescription("This is description");
        movie.setGenera(MovieGenera.ADVENTURE);
        movie.setTitle("Junit Movie");

        MovieEntity object = mapper.map(movie);

        assertEquals(movie.getTitle(), object.getTitle());
        assertEquals(movie.getId(), object.getId());
        assertEquals(movie.getGenera(), object.getGenera());
        assertEquals(movie.getDescription(), object.getDescription());
    }
}
