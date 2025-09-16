package org.example.jpasearchfiltersort.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.FetchParent;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.jpasearchfiltersort.markers.DtoMarker;
import org.example.jpasearchfiltersort.markers.EntityMarker;
import org.example.jpasearchfiltersort.service.filter.BuildPredicateFacade;
import org.example.jpasearchfiltersort.service.filter.BuildPredicateFacade.BuildPredicateParameters;
import org.example.jpasearchfiltersort.service.rule.PredicateRuleService;
import org.example.jpasearchfiltersort.service.rule.builder.FilterRule.FilterRuleConfig;
import org.example.jpasearchfiltersort.service.search.BuildSearchService;
import org.example.jpasearchfiltersort.service.search.BuildSearchService.BuildSearchParameters;
import org.example.jpasearchfiltersort.service.sort.BuildSortFacade;
import org.example.jpasearchfiltersort.service.sort.BuildSortFacade.BuildSortParameters;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Setter
@Getter
@Builder(setterPrefix = "set")
public class BasicSearchSpecification<TDto extends DtoMarker, TEntity extends EntityMarker>
        implements SearchSpecification<TEntity> {

    private Map<Class<?>, PredicateRuleService<TDto, TEntity>> predicateRuleServiceMap;

    private BuildPredicateFacade<TEntity> buildPredicateFacade;

    private BuildSearchService<TEntity> buildSearchService;

    private BuildSortFacade<TEntity> buildSortFacade;

    private SearchRequestInterface searchRequest;

    private Class<?> dtoClass;

    @Override
    @SuppressWarnings("NullableProblems")
    public Predicate toPredicate(Root<TEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.equal(cb.literal(Boolean.TRUE), Boolean.TRUE);
        PredicateRuleService<TDto, TEntity> predicateRuleService = getPagingRule();
        Map<String, FetchParent<?, ?>> body = predicateRuleService.registerBody(root, query).getQueryBodyMap();

        predicate = buildSearchService.build(predicate,
                                             BuildSearchParameters.of(root, body, getSearchRule(predicateRuleService),
                                                                      cb,
                                                                      searchRequest.getSearchBy()));

        predicate = buildPredicateFacade.build(predicate,
                                               BuildPredicateParameters.of(root, cb, searchRequest, body,
                                                                           getFilterRule(predicateRuleService)));

        buildSortFacade.build(query, BuildSortParameters.of(root, cb, searchRequest, body,
                                                            getSortRule(predicateRuleService)));
        return predicate;
    }

    private PredicateRuleService<TDto, TEntity> getPagingRule() {
        if (isFalse(predicateRuleServiceMap.containsKey(dtoClass))) {
            throw new IllegalArgumentException("Для объекта %s не реализован сервис поиска".formatted(dtoClass));
        }
        return predicateRuleServiceMap.get(dtoClass);
    }

    private Map<String, Function<From<?, ?>, List<Path<String>>>> getSearchRule(
            PredicateRuleService<TDto, TEntity> predicateRuleService) {
        return predicateRuleService.getSearchRule().getSearchRuleMap();
    }

    private Map<String, Map<String, Function<From<?, ?>, Path<?>>>> getSortRule(
            PredicateRuleService<TDto, TEntity> predicateRuleService) {
        return predicateRuleService.getSortRule().getSortRuleMap();
    }

    private Map<String, Map<String, FilterRuleConfig>> getFilterRule(
            PredicateRuleService<TDto, TEntity> predicateRuleService) {
        return predicateRuleService.getFilterRule().getFilterRuleMap();
    }

}
