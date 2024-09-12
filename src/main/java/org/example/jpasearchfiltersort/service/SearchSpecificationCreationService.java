package com.glowbyte.decision.core.service.search;

import com.glowbyte.decision.core.enums.DisplayMode;
import com.glowbyte.decision.core.enums.ObjectType;
import com.glowbyte.decision.core.model.search.SearchRequestInterface;
import com.glowbyte.decision.core.service.search.filter.BuildPredicateFacade;
import com.glowbyte.decision.core.service.search.rule.PredicateRuleService;
import com.glowbyte.decision.core.service.search.sort.BuildSortFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис создания SearchSpecification
 *
 * @param <T> - сущность (Diagram, Deploy etc)
 */
@Service
@RequiredArgsConstructor
public class SearchSpecificationCreationService<T> {

    private final Map<ObjectType, PredicateRuleService> pagingRuleMap;
    private final BuildPredicateFacade<T> buildPredicateFacade;
    private final BuildSearchService<T> buildSearchService;
    private final BuildSortFacade<T> buildSortFacade;

    public SearchSpecification<T> createSearchSpecification(SearchRequestInterface searchRequest, ObjectType objectType,
                                                            DisplayMode displayMode) {
        return BasicSearchSpecification.<T>builder()
                .setPagingRuleMap(pagingRuleMap)
                .setBuildPredicateFacade(buildPredicateFacade)
                .setBuildSearchService(buildSearchService)
                .setBuildSortFacade(buildSortFacade)
                .setSearchRequest(searchRequest)
                .setObjectType(objectType)
                .setDisplayMode(displayMode)
                .build();
    }

}
