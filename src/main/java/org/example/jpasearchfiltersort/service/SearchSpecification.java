package org.example.jpasearchfiltersort.service;

import org.springframework.data.jpa.domain.Specification;

public interface SearchSpecification<T> extends Specification<T> {

    void setSearchRequest(SearchRequestInterface listSearchRequest);

    void setDtoClass(Class<?> dtoClass);

}
