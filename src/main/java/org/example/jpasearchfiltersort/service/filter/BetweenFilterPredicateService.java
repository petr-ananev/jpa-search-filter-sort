package org.example.jpasearchfiltersort.service.filter;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.ConvertUtils;
import org.example.jpasearchfiltersort.enums.Operator;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static org.example.jpasearchfiltersort.enums.Operator.BETWEEN;


@Service
@RequiredArgsConstructor
public class BetweenFilterPredicateService implements BuildFilterPredicateService {
    @Override
    @SuppressWarnings("unchecked")
    public Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass) {
        Object value = ConvertUtils.convert(request.getValue(), valueClass);
        Object valueTo = ConvertUtils.convert(request.getValueTo(), valueClass);
        if (valueClass.isAssignableFrom(Timestamp.class)) {
            Timestamp startDate = (Timestamp) value;
            Timestamp endDate = (Timestamp) valueTo;
            return cb.and(cb.greaterThanOrEqualTo((Expression<Timestamp>) path, startDate),
                          cb.lessThanOrEqualTo((Expression<Timestamp>) path, endDate));
        }
        else if (valueClass.isAssignableFrom(Integer.class) || valueClass.isAssignableFrom(Double.class)) {
            Number start = (Number) value;
            Number end = (Number) valueTo;
            return cb.and(cb.ge((Expression<Number>) path, start), cb.le((Expression<Number>) path, end));
        } else {
            throw new IllegalArgumentException("Переданные в фильтр значения, не применимы к оператору Between");
        }
    }

    @Override
    public Operator getOperatorType() {
        return BETWEEN;
    }

}
