package com.ptv.livebox.movie.dao.entity;

import com.ptv.livebox.movie.dto.MovieGenera;

import javax.persistence.*;

@Entity(name = "movies")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @Enumerated(EnumType.STRING)
    private MovieGenera genera;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieGenera getGenera() {
        return genera;
    }

    public void setGenera(MovieGenera genera) {
        this.genera = genera;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
