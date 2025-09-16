package org.example.jpasearchfiltersort.dao;

import org.example.jpasearchfiltersort.service.SearchRequestInterface;
import org.springframework.data.domain.Page;

public interface PageSortAndFilterDaoManager<TEntity, TSearchRequest extends SearchRequestInterface> {

    Page<TEntity> getAll(Class<?> entityClass, Class<?> dtoClass, TSearchRequest searchRequest);

}
