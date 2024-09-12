package com.glowbyte.decision.core.service.search.filter;

import com.glowbyte.decision.core.enums.Operator;
import com.glowbyte.decision.core.model.search.FilterRequest;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public interface BuildFilterPredicateService {

    Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass);

    Operator getOperatorType();

}
