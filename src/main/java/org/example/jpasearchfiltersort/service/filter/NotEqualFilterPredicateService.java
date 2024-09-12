package org.example.jpasearchfiltersort.service.filter;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.ConvertUtils;
import org.example.jpasearchfiltersort.enums.Operator;
import org.springframework.stereotype.Service;

import static org.example.jpasearchfiltersort.enums.Operator.NOT_EQUAL;


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
