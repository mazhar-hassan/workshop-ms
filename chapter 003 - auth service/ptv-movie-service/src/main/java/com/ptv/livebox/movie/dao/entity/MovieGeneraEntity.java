package com.ptv.livebox.movie.dao.entity;

import com.ptv.livebox.common.api.movies.dtos.MovieGenera;

import javax.persistence.*;

@Entity(name = "tv_movie_generas")
public class MovieGeneraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private MovieGenera genera;
    @ManyToOne
    @JoinColumn(name = "movieId")
    private MovieEntity movie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieGenera getGenera() {
        return genera;
    }

    public void setGenera(MovieGenera genera) {
        this.genera = genera;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }
}

