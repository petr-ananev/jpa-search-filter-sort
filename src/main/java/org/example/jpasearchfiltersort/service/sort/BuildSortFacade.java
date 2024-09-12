package org.example.jpasearchfiltersort.service.sort;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.Coalesce;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.example.jpasearchfiltersort.enums.ObjectType;
import org.example.jpasearchfiltersort.service.SearchRequestInterface;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

/**
 * Фасад по билду сортировки
 * <p>
 * Состоит из:
 * Мапа по билду сортировки {@link #sortMap}
 * Билд предиката {@link #build(CriteriaQuery, BuildSortParameters)}
 * </p>
 *
 * @param <T> - сущность (Diagram, Deploy etc)
 */
@Service
@RequiredArgsConstructor
public class BuildSortFacade<T> {

    private final Map<Direction, BuildSortDirectionService> sortMap;

    public void build(CriteriaQuery<?> query, BuildSortParameters<T> parameters) {
        if (Objects.nonNull(parameters.getSearchRequest().getSorts())) {
            checkSortParameters(parameters);
            Map<String, From<?, ?>> body = parameters.getBody();
            Map<String, SortRequest> sortRequestByColumnName =
                    groupBySortRequestByColumnName(parameters.getSearchRequest().getSorts());
            List<Order> orders = sortRequestByColumnName.entrySet().stream().map((sortColumnToSortRequest -> {
                Map<String, Function<From<?, ?>, Path<?>>> joinPathToSortFunctionMap =
                        parameters.getSortRule().get(sortColumnToSortRequest.getKey());
                BuildSortDirectionService buildSortDirectionService =
                        getBuildSortDirectionServiceByDirection(sortColumnToSortRequest.getValue().getDirection());
                Coalesce orderByWithCoalesce = parameters.getCb().coalesce();
                joinPathToSortFunctionMap.entrySet().forEach(joinPathToSortFunction -> {
                    //Получаем path(root или join) запроса куда будем применять предикат
                    From<?, ?> pathToPredicateApply = body.get(joinPathToSortFunction.getKey());
                    orderByWithCoalesce.value(joinPathToSortFunction.getValue().apply(pathToPredicateApply));
                });
                return buildSortDirectionService.build(orderByWithCoalesce, parameters.getCb());
            })).collect(Collectors.toList());
            query.orderBy(orders);
        }
    }

    private void checkSortParameters(BuildSortParameters<T> parameters) {
        Set<String> configuredFilterColumns = parameters.getSortRule().keySet();
        Set<String> passedFilterColumns = parameters.getSearchRequest().getSorts().stream().map(
                                                            SortRequest::getColumnName)
                                                    .collect(Collectors.toSet());
        Collection<String> missingColumns = CollectionUtils.removeAll(passedFilterColumns, configuredFilterColumns);
        if (CollectionUtils.isNotEmpty(missingColumns)) {
            throw new IllegalArgumentException(
                    String.format("Для объекта - %s, не настроены правила сортировки для колонок - %s",
                                  parameters.getObjectType(), String.join(", ", missingColumns)));
        }
    }

    private BuildSortDirectionService getBuildSortDirectionServiceByDirection(Direction direction) {
        if (isFalse(sortMap.containsKey(direction))) {
            throw new IllegalArgumentException(
                    "Для оператора - %s, не настроена стратегия сортировки".formatted(direction));
        }
        return sortMap.get(direction);
    }

    private Map<String, SortRequest> groupBySortRequestByColumnName(List<SortRequest> filters) {
        return filters.stream().collect(Collectors.toMap(SortRequest::getColumnName, Function.identity()));
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class BuildSortParameters<T> {

        private Root<T> root;

        private ObjectType objectType;

        private CriteriaBuilder cb;

        private SearchRequestInterface searchRequest;

        private Map<String, From<?, ?>> body;

        private Map<String, Map<String, Function<From<?, ?>, Path<?>>>> sortRule;

    }

}
