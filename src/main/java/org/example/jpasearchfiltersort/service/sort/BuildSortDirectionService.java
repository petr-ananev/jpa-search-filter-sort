package com.glowbyte.decision.core.service.search.sort;

import org.springframework.data.domain.Sort.Direction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;

public interface BuildSortDirectionService {

    Order build(Expression<?> path, CriteriaBuilder cb);

    Direction getDirection();

}
