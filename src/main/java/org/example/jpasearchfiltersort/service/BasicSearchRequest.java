package org.example.jpasearchfiltersort.service;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Объект поиска. Прилетает заполненный с клиента.
 * <p>
 * Состоит из:
 * Коллекция фильтрации {@link #getFilters()}
 * Коллекция сортировки {@link #getSize()}
 * Отображаемая страница {@link #getPage()}
 * Кол-во элементов на странице {@link #getSize()}
 * Строка поиска {@link #getSearchBy()}
 * </p>
 */
@Data
@SuperBuilder(setterPrefix = "set")
@NoArgsConstructor
@AllArgsConstructor
@Convert(converter = ListSearchRequestConverter.class)
public class ListSearchRequest implements SearchRequestInterface {

    private List<FilterRequest> filters;

    private List<SortRequest> sorts;

    private Integer page;

    private Integer size;

    private String searchBy;

    public void addFilter(FilterRequest filterRequest) {
        if (Objects.isNull(this.filters)) {
            this.filters = new ArrayList<>();
        }
        this.filters.add(filterRequest);
    }

    public void addSort(SortRequest sortRequest) {
        if (Objects.isNull(this.sorts)) {
            this.sorts = new ArrayList<>();
        }
        this.sorts.add(sortRequest);
    }

}
