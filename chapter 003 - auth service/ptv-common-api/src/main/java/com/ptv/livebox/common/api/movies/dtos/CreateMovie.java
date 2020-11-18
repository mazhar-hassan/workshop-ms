package com.ptv.livebox.common.api.movies.dtos;

public class CreateMovie extends MovieDetail {
    private Publisher publisher;

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
