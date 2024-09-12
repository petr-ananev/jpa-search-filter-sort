package org.example.jpasearchfiltersort.service.filter;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.example.jpasearchfiltersort.enums.ObjectType;
import org.example.jpasearchfiltersort.enums.Operator;
import org.example.jpasearchfiltersort.service.SearchRequestInterface;
import org.example.jpasearchfiltersort.service.rule.builder.FilterRule.FilterRuleConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

/**
 * Фасад по билду предикатор для фильтрации
 * <p>
 * Состоит из:
 * Мапа по билду предиката {@link #predicateMap}
 * Билд предиката {@link #build(Predicate, BuildPredicateParameters)}
 * </p>
 *
 * @param <T> - сущность
 */
@Service
@RequiredArgsConstructor
public class BuildPredicateFacade<T> {

    private final Map<Operator, BuildFilterPredicateService> predicateMap;

    public Predicate build(Predicate predicate, BuildPredicateParameters<T> parameters) {
        if (Objects.nonNull(parameters.getSearchRequest().getFilters())) {
            checkPredicateParameters(parameters);
            List<Predicate> list = new ArrayList<>(List.of(predicate));
            Map<String, From<?, ?>> body = parameters.getBody();
            Map<String, List<FilterRequest>> filterRequestByColumnName =
                    groupByFilterRequestByColumnName(parameters.getSearchRequest().getFilters());
            CriteriaBuilder cb = parameters.getCb();

            parameters.getFilterRule().entrySet()
                      .stream()
                      .filter(e -> filterRequestByColumnName.containsKey(e.getKey()))
                      .forEach(columnNameToJoinPathToPredicateConfig -> {
                          String columnName = columnNameToJoinPathToPredicateConfig.getKey();
                          columnNameToJoinPathToPredicateConfig.getValue().forEach((joinPath, predicateConfig) -> {
                              //Получаем path(root или join) запроса куда будем применять предикат
                              From<?, ?> pathToPredicateApply = body.get(joinPath);
                              Expression<?> columnPredicate = predicateConfig.getExpression(pathToPredicateApply);

                              filterRequestByColumnName.get(columnName).forEach(filter -> {
                                  BuildFilterPredicateService buildFilterPredicateService =
                                          getBuildFilterPredicateServiceByOperator(filter.getOperator());

                                  Predicate filterPredicate = buildFilterPredicateService.build(columnPredicate, cb,
                                                                                                filter,
                                                                                                predicateConfig.getAttributeType());

                                  Predicate enrichedPredicate = cb.and(filterPredicate);
                                  list.add(cb.and(enrichedPredicate));
                              });
                          });
                      });
            Predicate[] p = new Predicate[list.size()];
            predicate = cb.and(list.toArray(p));
        }
        return predicate;
    }

    private void checkPredicateParameters(BuildPredicateParameters<T> parameters) {
        Set<String> configuredFilterColumns = parameters.getFilterRule().keySet();
        Set<String> passedFilterColumns = parameters.getSearchRequest().getFilters().stream().map(
                                                            FilterRequest::getColumnName)
                                                    .collect(Collectors.toSet());
        Collection<String> missingColumns = CollectionUtils.removeAll(passedFilterColumns, configuredFilterColumns);
        if (CollectionUtils.isNotEmpty(missingColumns)) {
            throw new IllegalArgumentException(
                    String.format("Для объекта - %s, не настроены правила фильтрации для колонок - %s",
                                  parameters.getObjectType(), String.join(", ", missingColumns)));
        }
    }


    private BuildFilterPredicateService getBuildFilterPredicateServiceByOperator(Operator operator) {
        if (isFalse(predicateMap.containsKey(operator))) {
            throw new IllegalArgumentException(
                    "Для оператора - %s, не настроена стратегия фильтрации".formatted(
                            operator));
        }
        return predicateMap.get(operator);
    }

    private Map<String, List<FilterRequest>> groupByFilterRequestByColumnName(List<FilterRequest> filters) {
        return filters.stream().collect(Collectors.groupingBy(FilterRequest::getColumnName));
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class BuildPredicateParameters<T> {

        private Root<T> root;

        private ObjectType objectType;

        private CriteriaBuilder cb;

        private SearchRequestInterface searchRequest;

        private Map<String, From<?, ?>> body;

        private Map<String, Map<String, FilterRuleConfig>> filterRule;

    }

}
