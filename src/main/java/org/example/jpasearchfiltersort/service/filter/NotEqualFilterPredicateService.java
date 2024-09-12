package com.glowbyte.decision.core.service.search.filter;

import com.glowbyte.decision.core.enums.Operator;
import com.glowbyte.decision.core.model.search.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.stereotype.Service;

import static com.glowbyte.decision.core.enums.Operator.NOT_EQUAL;

@Service
@RequiredArgsConstructor
public class NotEqualFilterPredicateService implements BuildFilterPredicateService {

    @Override
    public Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass) {
        Object value = ConvertUtils.convert(request.getValue(), valueClass);
        return cb.notEqual(path, value);
    }

    @Override
    public Operator getOperatorType() {
        return NOT_EQUAL;
    }

}
