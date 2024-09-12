package com.glowbyte.decision.core.service.search;

import com.glowbyte.decision.core.enums.DisplayMode;
import com.glowbyte.decision.core.enums.ObjectType;
import com.glowbyte.decision.core.error.SearchException;
import com.glowbyte.decision.core.model.search.SearchRequestInterface;
import com.glowbyte.decision.core.service.search.BuildSearchService.BuildSearchParameters;
import com.glowbyte.decision.core.service.search.builder.FilterRule.FilterRuleConfig;
import com.glowbyte.decision.core.service.search.filter.BuildPredicateFacade;
import com.glowbyte.decision.core.service.search.filter.BuildPredicateFacade.BuildPredicateParameters;
import com.glowbyte.decision.core.service.search.rule.PredicateRuleService;
import com.glowbyte.decision.core.service.search.sort.BuildSortFacade;
import com.glowbyte.decision.core.service.search.sort.BuildSortFacade.BuildSortParameters;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

/**
 * Данная спецификация нужна для осуществления фильтрации, пагинации и сортировки
 * <p>
 * Состоит из:
 * Фасад для фильтрации {@link #buildPredicateFacade}
 * Фасад для поиска {@link #buildSearchService}
 * Объет поиска {@link #searchRequest}
 * Билд предиката {@link #toPredicate(Root, CriteriaQuery, CriteriaBuilder)} ()}
 * </p>
 *
 * @param <T> - сущность (Diagram, Deploy etc)
 */
@Setter
@Getter
@Builder(setterPrefix = "set")
public class BasicSearchSpecification<T> implements SearchSpecification<T> {

    private final Map<ObjectType, PredicateRuleService> pagingRuleMap;
    private BuildPredicateFacade<T> buildPredicateFacade;
    private BuildSearchService<T> buildSearchService;
    private BuildSortFacade<T> buildSortFacade;
    private SearchRequestInterface searchRequest;
    private ObjectType objectType;
    private DisplayMode displayMode;

    @Override
    @SuppressWarnings("NullableProblems")
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.equal(cb.literal(Boolean.TRUE), Boolean.TRUE);
        PredicateRuleService predicateRuleService = getPagingRule();
        Map<String, From<?, ?>> body = predicateRuleService.registerBody(displayMode, root).getQueryBodyMap();

        predicate = buildSearchService.build(predicate,
                                             BuildSearchParameters.of(root, body, getSearchRule(predicateRuleService), cb,
                                                                      searchRequest.getSearchBy()));

        predicate = buildPredicateFacade.build(predicate,
                                               BuildPredicateParameters.of(root, objectType,
                                                                           displayMode, cb, searchRequest, body,
                                                                           getFilterRule(predicateRuleService)));

        buildSortFacade.build(query, BuildSortParameters.of(root, objectType, cb, searchRequest, body,
                                                            getSortRule(predicateRuleService)));
        return predicate;
    }

    private PredicateRuleService getPagingRule() {
        if (isFalse(pagingRuleMap.containsKey(objectType))) {
            throw new SearchException("Для объекта - %s, не заданы поля поиска".formatted(objectType));
        }
        return pagingRuleMap.get(objectType);
    }

    private Map<String, Function<From<?, ?>, List<Path<String>>>> getSearchRule(
            PredicateRuleService predicateRuleService) {
        return predicateRuleService.getSearchRule(displayMode).getSearchRuleMap();
    }

    private Map<String, Map<String, Function<From<?, ?>, Path<?>>>> getSortRule(
            PredicateRuleService predicateRuleService) {
        return predicateRuleService.getSortRule(displayMode).getSortRuleMap();
    }

    private Map<String, Map<String, FilterRuleConfig>> getFilterRule(
            PredicateRuleService predicateRuleService) {
        return predicateRuleService.getFilterRule(displayMode).getFilterRuleMap();
    }

}
