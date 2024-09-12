package org.example.jpasearchfiltersort.service.search;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Сервис по билду поиска
 * <p>
 * Состоит из:
 * Билд предиката {@link #build(Predicate, BuildSearchParameters)}
 * </p>
 *
 * @param <T> - сущность (Diagram, Deploy etc)
 */
@Service
@RequiredArgsConstructor
public class BuildSearchService<T> {

    public Predicate build(Predicate predicate, BuildSearchParameters<T> parameters) {
        if (StringUtils.isEmpty(parameters.getSearchBy())) {
            return predicate;
        }
        Map<String, From<?, ?>> body = parameters.getBody();
        Map<String, Function<From<?, ?>, List<Path<String>>>> searchRule = parameters.getSearchRule();
        List<Expression<String>> searchRules = new ArrayList<>();
        //Ходим по мапе предикатов поиска, где ключ - path(root или join) запроса, значение колонка к которой надо применить like
        searchRule.forEach((joinKey, predicateFunction) -> {
            //Получаем path(root или join) запроса куда будем применять предикат
            From<?, ?> pathToPredicateApply = body.get(joinKey);
            //Получаем path(root или join) до самой колонки
            List<Path<String>> apply = predicateFunction.apply(pathToPredicateApply);
            searchRules.addAll(apply);
        });
        CriteriaBuilder cb = parameters.getCb();
        return cb.and(cb.or(searchRules.stream()
                                       .map(f -> cb.like(cb.upper(f),
                                                         "%" + parameters.getSearchBy().toUpperCase() + "%"))
                                       .toArray(Predicate[]::new)), predicate);
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class BuildSearchParameters<T> {

        private Root<T> root;

        private CriteriaBuilder cb;

        private String searchBy;

        private Map<String, From<?, ?>> body;

        private Map<String, Function<From<?, ?>, List<Path<String>>>> searchRule;

    }

}
