package org.example.jpasearchfiltersort.service.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.example.jpasearchfiltersort.enums.Operator;

public interface BuildFilterPredicateService {

    Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass);

    Operator getOperatorType();

}
