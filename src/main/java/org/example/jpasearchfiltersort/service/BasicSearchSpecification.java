package org.example.jpasearchfiltersort.service;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.jpasearchfiltersort.enums.ObjectType;
import org.example.jpasearchfiltersort.service.filter.BuildPredicateFacade;
import org.example.jpasearchfiltersort.service.filter.BuildPredicateFacade.BuildPredicateParameters;
import org.example.jpasearchfiltersort.service.rule.PredicateRuleService;
import org.example.jpasearchfiltersort.service.search.BuildSearchService;
import org.example.jpasearchfiltersort.service.search.BuildSearchService.BuildSearchParameters;
import org.example.jpasearchfiltersort.service.sort.BuildSortFacade;
import org.example.jpasearchfiltersort.service.sort.BuildSortFacade.BuildSortParameters;

import java.util.Map;

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

    private Map<ObjectType, PredicateRuleService> predicateRuleServiceMap;

    private BuildPredicateFacade<T> buildPredicateFacade;

    private BuildSearchService<T> buildSearchService;

    private BuildSortFacade<T> buildSortFacade;

    private SearchRequestInterface searchRequest;

    private ObjectType objectType;


    @Override
    @SuppressWarnings("NullableProblems")
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.equal(cb.literal(Boolean.TRUE), Boolean.TRUE);
        if (isFalse(predicateRuleServiceMap.containsKey(objectType))) {
            throw new IllegalArgumentException("Для объекта - %s, не настроена стратегия фильтрации".formatted(objectType));
        }
        PredicateRuleService predicateRuleService = predicateRuleServiceMap.get(objectType);
        Map<String, From<?, ?>> body = predicateRuleService.registerBody(root).getQueryBodyMap();

        predicate = buildSearchService.build(predicate,
                                             BuildSearchParameters.of(root, cb, searchRequest.getSearchBy(), body
                                                     , predicateRuleService.getSearchRule().getSearchRuleMap()));

        predicate = buildPredicateFacade.build(predicate,
                                               BuildPredicateParameters.of(root, objectType,
                                                                           cb, searchRequest, body,
                                                                           predicateRuleService.getFilterRule().getFilterRuleMap()));

        buildSortFacade.build(query, BuildSortParameters.of(root, objectType, cb, searchRequest, body,
                                                            predicateRuleService.getSortRule().getSortRuleMap()));
        return predicate;
    }

}
