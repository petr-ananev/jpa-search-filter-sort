package com.glowbyte.decision.core.service.search.sort;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;

@Service
public class AscDirectionServiceBuild implements BuildSortDirectionService {

    @Override
    public Order build(Expression<?> path, CriteriaBuilder cb) {
        return cb.asc(path);
    }

    @Override
    public Direction getDirection() {
        return Direction.ASC;
    }

}
