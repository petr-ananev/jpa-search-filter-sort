package com.glowbyte.decision.core.service.search.filter;

import com.glowbyte.decision.core.enums.Operator;
import com.glowbyte.decision.core.model.search.FilterRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import static com.glowbyte.decision.core.enums.Operator.EQUAL;

@Service
@RequiredArgsConstructor
public class EqualFilterPredicateService implements BuildFilterPredicateService {

    @Override
    public Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass) {
        Object value = ConvertUtils.convert(request.getValue(), valueClass);
        return cb.equal(path, value);
    }

    @Override
    public Operator getOperatorType() {
        return EQUAL;
    }

}
