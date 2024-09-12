package org.example.jpasearchfiltersort.service;

import org.example.jpasearchfiltersort.enums.ObjectType;
import org.springframework.data.jpa.domain.Specification;

public interface SearchSpecification<T> extends Specification<T> {

    void setSearchRequest(SearchRequestInterface listSearchRequest);

    void setObjectType(ObjectType objectType);

}
