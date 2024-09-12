package com.glowbyte.decision.core.service.search.filter;

import com.glowbyte.decision.core.enums.Operator;
import com.glowbyte.decision.core.model.search.FilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
public class IsNullFilterPredicateService implements BuildFilterPredicateService {

    @Override
    public Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass) {
        return cb.isNull(path);
    }

    @Override
    public Operator getOperatorType() {
        return Operator.IS_NULL;
    }

}
