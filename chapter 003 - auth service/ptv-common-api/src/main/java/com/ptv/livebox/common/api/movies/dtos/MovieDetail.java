package com.ptv.livebox.common.api.movies.dtos;

import java.util.List;

public class MovieDetail {
    private String title;
    private List<MovieGenera> generas;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MovieGenera> getGeneras() {
        return generas;
    }

    public void setGeneras(List<MovieGenera> generas) {
        this.generas = generas;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
