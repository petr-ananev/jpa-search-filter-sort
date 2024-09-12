package com.glowbyte.decision.core.service.search.filter;

import com.glowbyte.decision.core.enums.Operator;
import com.glowbyte.decision.core.error.FilterException;
import com.glowbyte.decision.core.model.search.FilterRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.List;

import static com.glowbyte.decision.core.enums.Operator.IN;

@Service
@RequiredArgsConstructor
public class InFilterPredicateService implements BuildFilterPredicateService {

    @Override
    public Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass) {
        if (CollectionUtils.isEmpty(request.getValues())) {
            throw new FilterException("Коллекция значений переданная в оператор In не может быть пустой");
        }
        List<Object> values = request.getValues();
        CriteriaBuilder.In<Object> inClause = cb.in(path);
        for (Object value : values) {
            inClause.value(ConvertUtils.convert(value, valueClass));
        }
        return inClause;
    }

    @Override
    public Operator getOperatorType() {
        return IN;
    }

}
