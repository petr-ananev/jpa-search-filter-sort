package org.example.jpasearchfiltersort.service.sort;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class DescDirectionServiceBuild implements BuildSortDirectionService {

    @Override
    public Order build(Expression<?> path, CriteriaBuilder cb) {
        return cb.desc(path);
    }

    @Override
    public Direction getDirection() {
        return Direction.DESC;
    }

}
