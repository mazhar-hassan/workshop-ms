package com.ptv.livebox.movie.dto;

public class Movie {
    private Integer id;
    private String title;
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
