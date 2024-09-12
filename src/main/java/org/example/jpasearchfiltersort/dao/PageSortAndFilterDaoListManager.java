package com.glowbyte.decision.core.dao;

import com.glowbyte.decision.core.enums.ObjectType;
import com.glowbyte.decision.core.model.search.AdditionalFilterInterface;
import com.glowbyte.decision.core.model.search.ListSearchRequest;
import com.glowbyte.decision.core.model.search.SearchRequestInterface;
import com.glowbyte.decision.core.service.search.SearchRequestFactory;
import com.glowbyte.decision.core.service.search.SearchSpecificationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

import static com.glowbyte.decision.core.enums.DisplayMode.LIST;

/**
 * Сервис - прослойка для вызова метода репозитория из переданного дао
 * Для каждой сущность @Bean создается через конфигурацию
 * <p>
 * Состоит из:
 * Имплементация дао с пагинацией, фильтрацией и сортировкой {@link #decisionPageSortAndFilterDao} Передается в конструктор
 * Получение отфильтрованной страницы {@link #getAllNonHistorical(ObjectType, ListSearchRequest)}
 * Получение отфильтрованной страницы для исторической сущности {@link #getAllHistorical(ObjectType, ListSearchRequest)}
 * </p>
 *
 * @param <T> - сущность (Diagram, Deploy etc)
 */
@RequiredArgsConstructor
public class DecisionPageSortAndFilterDaoListManager<T>
        implements DecisionPageSortAndFilterDaoManager<T, ListSearchRequest> {

    private final DecisionReadAllPageSortAndFilterDao<T> decisionPageSortAndFilterDao;
    private final SearchSpecificationCreationService<T> searchSpecificationCreationService;
    private final SearchRequestFactory<ListSearchRequest> searchRequestFactory;
    private final Map<ObjectType, List<AdditionalFilterInterface<SearchRequestInterface>>> additionalFilters;

    @Override
    public Page<T> getAllNonHistoricalLifeCycle(ObjectType objectType, ListSearchRequest listSearchRequest) {
        listSearchRequest = searchRequestFactory.getDefaultLifeCycleSearchRequestIfNull(listSearchRequest);
        return getAllBase(objectType, listSearchRequest);
    }

    @Override
    public Page<T> getAllNonHistorical(ObjectType objectType, ListSearchRequest listSearchRequest) {
        listSearchRequest = searchRequestFactory.getDefaultSearchRequestIfNull(listSearchRequest);
        return getAllBase(objectType, listSearchRequest);
    }

    @Override
    public Page<T> getAllHistorical(ObjectType objectType, ListSearchRequest listSearchRequest) {
        listSearchRequest = searchRequestFactory.getDefaultHistoricalSearchRequestIfNull(listSearchRequest);
        return getAllBase(objectType, listSearchRequest);
    }

    @Override
    public Page<T> getVersions(ObjectType objectType, ListSearchRequest listSearchRequest) {
        listSearchRequest = searchRequestFactory.getDefaultVersionsSearchRequestIfNull(listSearchRequest);
        return getAllBase(objectType, listSearchRequest);
    }

    /**
     * Отнимаем 1 т.к фронт шлет страницы начиная с 1
     */
    private Page<T> getAllBase(ObjectType objectType, ListSearchRequest listSearchRequest) {
        if (additionalFilters.containsKey(objectType)) {
            additionalFilters.get(objectType).forEach(filter -> filter.addAdditionalFilter(listSearchRequest));
        }
        return decisionPageSortAndFilterDao.getAll(
                searchSpecificationCreationService.createSearchSpecification(listSearchRequest, objectType, LIST),
                PageRequest.of(listSearchRequest.getPage() - 1, listSearchRequest.getSize()));
    }

}
