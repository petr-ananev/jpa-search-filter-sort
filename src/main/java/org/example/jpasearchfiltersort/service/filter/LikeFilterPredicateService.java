package org.example.jpasearchfiltersort.service.filter;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.example.jpasearchfiltersort.enums.Operator;
import org.springframework.stereotype.Service;

import static org.example.jpasearchfiltersort.enums.Operator.LIKE;


@Service
public class LikeFilterPredicateService implements BuildFilterPredicateService {

    @Override
    @SuppressWarnings("unchecked")
    public Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass) {
        return cb.like(cb.upper((Expression<String>) path), "%%%s%%".formatted(request.getValue().toString().toUpperCase()));
    }

    @Override
    public Operator getOperatorType() {
        return LIKE;
    }

}
