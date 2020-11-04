package com.ptv.livebox.common.api.movies.dtos;

public class MovieDetail {
    private String title;
    private MovieGenera genera;
    private String description;

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
