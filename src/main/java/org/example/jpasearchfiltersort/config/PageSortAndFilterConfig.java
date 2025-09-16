package org.example.jpasearchfiltersort.config;

import org.example.jpasearchfiltersort.dao.ReadAllPageSortAndFilterDao;
import org.example.jpasearchfiltersort.enums.Operator;
import org.example.jpasearchfiltersort.markers.DtoMarker;
import org.example.jpasearchfiltersort.markers.EntityMarker;
import org.example.jpasearchfiltersort.service.filter.BuildFilterPredicateService;
import org.example.jpasearchfiltersort.service.rule.PredicateRuleService;
import org.example.jpasearchfiltersort.service.sort.BuildSortDirectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.example.jpasearchfiltersort.utils.ClassUtils.resolveGenericType;

@Configuration
public class PageSortAndFilterConfig {

//    @Bean
//    public PageSortAndFilterDaoManager<SimpleEntity, BasicSearchRequest> getCustomAttributeDictPageSortAndFilterDaoFacade(
//            ReadAllPageSortAndFilterDao<SimpleEntity> decisionPageableDao,
//            SearchRequestFactory<BasicSearchRequest> searchRequestFactory,
//            SearchSpecificationCreationService<SimpleEntity> searchSpecificationCreationService) {
//        return new PageSortAndFilterDaoListManager<>(decisionPageableDao,
//                                                     searchSpecificationCreationService,
//                                                     searchRequestFactory);
//    }

    @Bean
    public <TEntity extends EntityMarker> Map<Class<?>, ReadAllPageSortAndFilterDao<TEntity>> getReadAllDaoMap(
            Set<ReadAllPageSortAndFilterDao<TEntity>> predicateRuleServices) {
        return predicateRuleServices.stream()
                                    .collect(toMap(service ->
                                                           resolveGenericType(service, 0, ReadAllPageSortAndFilterDao.class),
                                                   identity()));
    }

    @Bean
    public <TDto extends DtoMarker, TEntity extends EntityMarker> Map<Class<?>, PredicateRuleService<TDto, TEntity>> getPagingRuleServiceMap(
            Set<PredicateRuleService<TDto, TEntity>> predicateRuleServices) {
        return predicateRuleServices.stream()
                                    .collect(toMap(service ->
                                                           resolveGenericType(service, 0, PredicateRuleService.class),
                                                   identity()));
    }

    @Bean
    public Map<Operator, BuildFilterPredicateService> getBuildPredicateServiceMap(
            Set<BuildFilterPredicateService> serviceSet) {
        return serviceSet.stream().collect(toMap(BuildFilterPredicateService::getOperatorType, Function.identity()));
    }

    @Bean
    public Map<Direction, BuildSortDirectionService> getSortDirectionServiceMap(
            Set<BuildSortDirectionService> serviceSet) {
        return serviceSet.stream().collect(toMap(BuildSortDirectionService::getDirection, Function.identity()));
    }


}
