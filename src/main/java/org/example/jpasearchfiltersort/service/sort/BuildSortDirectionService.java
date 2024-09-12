package org.example.jpasearchfiltersort.service.sort;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import org.springframework.data.domain.Sort.Direction;

public interface BuildSortDirectionService {

    Order build(Expression<?> path, CriteriaBuilder cb);

    Direction getDirection();

}
