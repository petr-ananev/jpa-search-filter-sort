package com.glowbyte.decision.core.service.search.filter;


import com.glowbyte.decision.core.enums.Operator;
import com.glowbyte.decision.core.error.FilterException;
import com.glowbyte.decision.core.model.search.FilterRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.sql.Timestamp;

import static com.glowbyte.decision.core.enums.Operator.BETWEEN;

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
            throw new FilterException("Переданные в фильтр значения, не применимы к оператору Between");
        }
    }

    @Override
    public Operator getOperatorType() {
        return BETWEEN;
    }

}
