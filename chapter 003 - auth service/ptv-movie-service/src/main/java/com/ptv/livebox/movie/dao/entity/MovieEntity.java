package com.ptv.livebox.movie.dao.entity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "tv_movies")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<MovieGeneraEntity> generas;
    private String description;
    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PublisherEntity publisher;

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

    public List<MovieGeneraEntity> getGeneras() {
        return generas;
    }

    public void setGeneras(List<MovieGeneraEntity> generas) {
        this.generas = generas;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PublisherEntity getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherEntity publisher) {
        this.publisher = publisher;
    }
}
