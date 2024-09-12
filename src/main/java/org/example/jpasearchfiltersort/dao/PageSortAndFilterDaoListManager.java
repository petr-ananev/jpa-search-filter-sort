package org.example.jpasearchfiltersort.dao;


import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.enums.ObjectType;
import org.example.jpasearchfiltersort.service.BasicSearchRequest;
import org.example.jpasearchfiltersort.service.SearchRequestFactory;
import org.example.jpasearchfiltersort.service.SearchSpecificationCreationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


/**
 * Сервис - прослойка для вызова метода репозитория из переданного дао
 * Для каждой сущность @Bean создается через конфигурацию
 *
 * @param <T> - сущность
 */
@RequiredArgsConstructor
public class PageSortAndFilterDaoListManager<T>
        implements PageSortAndFilterDaoManager<T, BasicSearchRequest> {

    private final ReadAllPageSortAndFilterDao<T> decisionPageSortAndFilterDao;

    private final SearchSpecificationCreationService<T> searchSpecificationCreationService;

    private final SearchRequestFactory<BasicSearchRequest> searchRequestFactory;

    @Override
    public Page<T> getAll(ObjectType objectType, BasicSearchRequest basicSearchRequest) {
        basicSearchRequest = searchRequestFactory.getDefaultSearchRequestIfNull(basicSearchRequest);
        return getAllBase(objectType, basicSearchRequest);
    }

    private Page<T> getAllBase(ObjectType objectType, BasicSearchRequest basicSearchRequest) {
        return decisionPageSortAndFilterDao.getAll(
                searchSpecificationCreationService.createSearchSpecification(basicSearchRequest, objectType),
                PageRequest.of(basicSearchRequest.getPage(), basicSearchRequest.getSize()));
    }

}
