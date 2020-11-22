package com.ptv.livebox.movie.dao;

import com.ptv.livebox.movie.dao.entity.MovieGeneraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGeneraRepository extends JpaRepository<MovieGeneraEntity, Long> {
    List<MovieGeneraEntity> findAllByMovie_Id(Integer movieId);
}
