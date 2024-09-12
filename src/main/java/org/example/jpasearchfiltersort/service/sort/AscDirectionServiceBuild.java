package org.example.jpasearchfiltersort.service.sort;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

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
