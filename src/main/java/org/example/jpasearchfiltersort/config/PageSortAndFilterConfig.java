package org.example.jpasearchfiltersort.config;

import org.example.jpasearchfiltersort.dao.PageSortAndFilterDaoListManager;
import org.example.jpasearchfiltersort.dao.PageSortAndFilterDaoManager;
import org.example.jpasearchfiltersort.dao.ReadAllPageSortAndFilterDao;
import org.example.jpasearchfiltersort.enums.ObjectType;
import org.example.jpasearchfiltersort.enums.Operator;
import org.example.jpasearchfiltersort.model.SimpleEntity;
import org.example.jpasearchfiltersort.service.BasicSearchRequest;
import org.example.jpasearchfiltersort.service.SearchRequestFactory;
import org.example.jpasearchfiltersort.service.SearchSpecificationCreationService;
import org.example.jpasearchfiltersort.service.filter.BuildFilterPredicateService;
import org.example.jpasearchfiltersort.service.rule.PredicateRuleService;
import org.example.jpasearchfiltersort.service.sort.BuildSortDirectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Configuration
public class PageSortAndFilterConfig {

    @Bean
    public PageSortAndFilterDaoManager<SimpleEntity, BasicSearchRequest> getCustomAttributeDictPageSortAndFilterDaoFacade(
            ReadAllPageSortAndFilterDao<SimpleEntity> decisionPageableDao,
            SearchRequestFactory<BasicSearchRequest> searchRequestFactory,
            SearchSpecificationCreationService<SimpleEntity> searchSpecificationCreationService) {
        return new PageSortAndFilterDaoListManager<>(decisionPageableDao,
                                                     searchSpecificationCreationService,
                                                     searchRequestFactory);
    }

    @Bean
    public Map<ObjectType, PredicateRuleService> getPagingRule(Set<PredicateRuleService> predicateRuleServices) {
        return predicateRuleServices.stream()
                                    .collect(toMap(PredicateRuleService::getObjectType, Function.identity()));
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
