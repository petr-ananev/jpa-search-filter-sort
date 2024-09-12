package org.example.jpasearchfiltersort.dao;

import org.example.jpasearchfiltersort.service.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ReadAllPageSortAndFilterDao<T> {

    /**
     * Метод получения отфильтрованного списка с пагинацией
     *
     * @return Page T объектов
     */
    Page<T> getAll(SearchSpecification<T> searchSpecification, PageRequest pageRequest);

}
