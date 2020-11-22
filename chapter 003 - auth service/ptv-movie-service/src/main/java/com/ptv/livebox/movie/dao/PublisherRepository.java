package com.ptv.livebox.movie.dao;

import com.ptv.livebox.movie.dao.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Integer> {
    PublisherEntity findByMovie_Id(Integer movieId);
}
