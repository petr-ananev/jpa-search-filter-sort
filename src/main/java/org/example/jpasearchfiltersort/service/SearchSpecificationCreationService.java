package org.example.jpasearchfiltersort.service;


import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.markers.DtoMarker;
import org.example.jpasearchfiltersort.markers.EntityMarker;
import org.example.jpasearchfiltersort.service.filter.BuildPredicateFacade;
import org.example.jpasearchfiltersort.service.rule.PredicateRuleService;
import org.example.jpasearchfiltersort.service.search.BuildSearchService;
import org.example.jpasearchfiltersort.service.sort.BuildSortFacade;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class SearchSpecificationCreationService<TDto extends DtoMarker, TEntity extends EntityMarker> {

    private final Map<Class<?>, PredicateRuleService<TDto, TEntity>> predicateRuleServiceMap;

    private final BuildPredicateFacade<TEntity> buildPredicateFacade;

    private final BuildSearchService<TEntity> buildSearchService;

    private final BuildSortFacade<TEntity> buildSortFacade;

    public SearchSpecification<TEntity> createSearchSpecification(SearchRequestInterface searchRequest, Class<?> dtoClass) {
        return BasicSearchSpecification.<TDto, TEntity>builder()
                .setPredicateRuleServiceMap(predicateRuleServiceMap)
                .setBuildPredicateFacade(buildPredicateFacade)
                .setBuildSearchService(buildSearchService)
                .setBuildSortFacade(buildSortFacade)
                .setSearchRequest(searchRequest)
                .setDtoClass(dtoClass)
                .build();
    }

}
