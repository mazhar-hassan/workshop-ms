package com.ptv.livebox.movie.dao;

import com.o4.microservices.common.utils.ValueUtils;
import com.ptv.livebox.movie.dao.entity.MovieEntity;
import com.ptv.livebox.movie.dto.MovieSearchRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MovieSpecifications implements Specification<MovieEntity> {

    private final MovieSearchRequest request;

    public MovieSpecifications(MovieSearchRequest request) {
        this.request = request;
    }

    @Override
    public Predicate toPredicate(Root<MovieEntity> root,
                                 CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (!ValueUtils.isEmpty(request.getTitle())) {
            predicates.add(criteriaBuilder.like(root.get("title"), request.getTitle()+"%"));
        }

        if (null != request.getGenera()) {
            predicates.add(criteriaBuilder.equal(root.get("generas").get("genera"),
                    request.getGenera()));
        }
        // all other filters ...

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
