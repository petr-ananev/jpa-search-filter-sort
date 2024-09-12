package org.example.jpasearchfiltersort.service;


import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.enums.ObjectType;
import org.example.jpasearchfiltersort.service.filter.BuildPredicateFacade;
import org.example.jpasearchfiltersort.service.rule.PredicateRuleService;
import org.example.jpasearchfiltersort.service.search.BuildSearchService;
import org.example.jpasearchfiltersort.service.sort.BuildSortFacade;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис создания SearchSpecification
 *
 * @param <T> - сущность
 */
@Service
@RequiredArgsConstructor
public class SearchSpecificationCreationService<T> {

    private final Map<ObjectType, PredicateRuleService> predicateRuleServiceMap;

    private final BuildPredicateFacade<T> buildPredicateFacade;

    private final BuildSearchService<T> buildSearchService;

    private final BuildSortFacade<T> buildSortFacade;

    public SearchSpecification<T> createSearchSpecification(SearchRequestInterface searchRequest, ObjectType objectType) {
        return BasicSearchSpecification.<T>builder()
                .setPredicateRuleServiceMap(predicateRuleServiceMap)
                .setBuildPredicateFacade(buildPredicateFacade)
                .setBuildSearchService(buildSearchService)
                .setBuildSortFacade(buildSortFacade)
                .setSearchRequest(searchRequest)
                .setObjectType(objectType)
                .build();
    }

}
