package org.example.jpasearchfiltersort.service.filter;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.ConvertUtils;
import org.example.jpasearchfiltersort.enums.Operator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.example.jpasearchfiltersort.enums.Operator.IN;

@Service
@RequiredArgsConstructor
public class InFilterPredicateService implements BuildFilterPredicateService {

    @Override
    public Predicate build(Expression<?> path, CriteriaBuilder cb, FilterRequest request, Class<?> valueClass) {
        if (CollectionUtils.isEmpty(request.getValues())) {
            throw new IllegalArgumentException("Коллекция значений переданная в оператор In не может быть пустой");
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
