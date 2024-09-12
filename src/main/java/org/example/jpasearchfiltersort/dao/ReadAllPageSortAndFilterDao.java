package com.glowbyte.decision.core.dao;

import com.glowbyte.decision.core.service.search.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DecisionReadAllPageSortAndFilterDao<T> {

    /**
     * Метод получения отфильтрованного списка с пагинацией
     *
     * @return Page T объектов
     */
    Page<T> getAll(SearchSpecification<T> searchSpecification, PageRequest pageRequest);

}
