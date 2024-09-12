package com.glowbyte.decision.core.service.search.filter;

import com.glowbyte.decision.core.enums.DisplayMode;
import com.glowbyte.decision.core.enums.FilterStrategy;
import com.glowbyte.decision.core.enums.ObjectType;
import com.glowbyte.decision.core.enums.Operator;
import com.glowbyte.decision.core.error.FilterException;
import com.glowbyte.decision.core.model.search.FilterRequest;
import com.glowbyte.decision.core.model.search.SearchRequestInterface;
import com.glowbyte.decision.core.service.search.builder.FilterRule.FilterRuleConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
 * @param <T> - сущность (Diagram, Deploy etc)
 */
@Service
@RequiredArgsConstructor
public class BuildPredicateFacade<T> {

    private final Map<Operator, BuildFilterPredicateService> predicateMap;
    private final Map<FilterStrategy, EnrichFilterStrategy> enrichFilterStrategyMap;

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
                              EnrichFilterStrategy enrichFilterStrategy = getEnrichFilterStrategyByFilterStrategy(
                                      predicateConfig.getFilterStrategy());

                              filterRequestByColumnName.get(columnName).forEach(filter -> {
                                  BuildFilterPredicateService buildFilterPredicateService =
                                          getBuildFilterPredicateServiceByOperator(filter.getOperator());

                                  Predicate filterPredicate = buildFilterPredicateService.build(columnPredicate, cb, filter,
                                                                                                predicateConfig.getAttributeType());
                                  if (Boolean.TRUE.equals(filter.getIsNullable())) {
                                      filterPredicate = cb.or(filterPredicate, cb.isNull(columnPredicate));
                                  }

                                  Predicate enrichedPredicate = enrichFilterStrategy.enrichPredicate(cb, filterPredicate,
                                                                                                     columnPredicate);
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
        Set<String> passedFilterColumns = parameters.getSearchRequest().getFilters().stream().map(FilterRequest::getColumnName)
                                                    .collect(Collectors.toSet());
        Collection<String> missingColumns = CollectionUtils.removeAll(passedFilterColumns, configuredFilterColumns);
        if (CollectionUtils.isNotEmpty(missingColumns)) {
            throw new FilterException(
                    String.format("Для объекта - %s, не настроены правила фильтрации для колонок - %s",
                                  parameters.getObjectType(), String.join(", ", missingColumns)));
        }
    }

    private EnrichFilterStrategy getEnrichFilterStrategyByFilterStrategy(FilterStrategy filterStrategy) {
        if (isFalse(enrichFilterStrategyMap.containsKey(filterStrategy))) {
            throw new FilterException(
                    "Для типа фильтрации - %s, не настроены правила обогащения фильтрации".formatted(
                            filterStrategy));
        }
        return enrichFilterStrategyMap.get(filterStrategy);
    }

    private BuildFilterPredicateService getBuildFilterPredicateServiceByOperator(Operator operator) {
        if (isFalse(predicateMap.containsKey(operator))) {
            throw new FilterException(
                    "Для оператора - %s, не настроена стратегия фильтрации".formatted(
                            operator));
        }
        return predicateMap.get(operator);
    }

    private Map<String, List<FilterRequest>> groupByFilterRequestByColumnName(List<FilterRequest> filters) {
        return filters.stream().collect(Collectors.groupingBy(FilterRequest::getColumnName));
    }

    @Getter
    @AllArgsConstructor
    public static class BuildPredicateParameters<T> {

        private Root<T> root;
        private ObjectType objectType;
        private DisplayMode displayMode;
        private CriteriaBuilder cb;
        private SearchRequestInterface searchRequest;
        private Map<String, From<?, ?>> body;
        private Map<String, Map<String, FilterRuleConfig>> filterRule;

        public static <T> BuildPredicateParameters<T> of(Root<T> root, ObjectType objectType,
                                                         DisplayMode displayMode,
                                                         CriteriaBuilder cb,
                                                         SearchRequestInterface searchRequest,
                                                         Map<String, From<?, ?>> body,
                                                         Map<String, Map<String, FilterRuleConfig>> filterRule) {
            return new BuildPredicateParameters<>(root, objectType, displayMode, cb, searchRequest, body, filterRule);
        }

    }

}
