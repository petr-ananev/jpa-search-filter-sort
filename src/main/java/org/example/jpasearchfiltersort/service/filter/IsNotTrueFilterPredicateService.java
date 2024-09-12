package org.example.jpasearchfiltersort.service.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.example.jpasearchfiltersort.enums.Operator;
import org.springframework.stereotype.Service;

import static org.example.jpasearchfiltersort.enums.Operator.IS_NOT_TRUE;


@Service
public class IsNotTrueFilterPredicateService implements BuildFilterPredicateService {

    @Override
    public Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass) {
        return cb.or(cb.isFalse((Expression<Boolean>) path), cb.isNull(path));
    }

    @Override
    public Operator getOperatorType() {
        return IS_NOT_TRUE;
    }

}
