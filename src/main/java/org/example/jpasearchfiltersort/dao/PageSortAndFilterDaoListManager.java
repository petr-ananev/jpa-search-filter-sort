package org.example.jpasearchfiltersort.dao;


import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.markers.DtoMarker;
import org.example.jpasearchfiltersort.markers.EntityMarker;
import org.example.jpasearchfiltersort.service.BasicSearchRequest;
import org.example.jpasearchfiltersort.service.SearchRequestFactory;
import org.example.jpasearchfiltersort.service.SearchSpecificationCreationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class PageSortAndFilterDaoListManager<TDto extends DtoMarker, TEntity extends EntityMarker>
        implements PageSortAndFilterDaoManager<TEntity, BasicSearchRequest> {

    private final Map<Class<?>, ReadAllPageSortAndFilterDao<TEntity>> decisionPageSortAndFilterDao;

    private final SearchSpecificationCreationService<TDto, TEntity> searchSpecificationCreationService;

    private final SearchRequestFactory<BasicSearchRequest> searchRequestFactory;

    @Override
    public Page<TEntity> getAll(Class<?> entityClass, Class<?> dtoClass, BasicSearchRequest basicSearchRequest) {
        basicSearchRequest = searchRequestFactory.getDefaultSearchRequestIfNull(basicSearchRequest);
        return getAllBase(entityClass, dtoClass, basicSearchRequest);
    }

    private Page<TEntity> getAllBase(Class<?> entityClass, Class<?> dtoClass, BasicSearchRequest basicSearchRequest) {
        return decisionPageSortAndFilterDao.get(entityClass)
                                           .getAll(searchSpecificationCreationService.createSearchSpecification(
                                                           basicSearchRequest, dtoClass),
                                                   PageRequest.of(basicSearchRequest.getPage(),
                                                                  basicSearchRequest.getSize()));
    }

}
