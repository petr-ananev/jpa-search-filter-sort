package com.glowbyte.decision.core.service.search.filter;

import com.glowbyte.decision.core.enums.Operator;
import com.glowbyte.decision.core.model.search.FilterRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import static com.glowbyte.decision.core.enums.Operator.IS_NOT_TRUE;

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
