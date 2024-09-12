package com.glowbyte.decision.core.model.search;


import java.util.List;

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

public interface SearchRequestInterface {

    List<FilterRequest> getFilters();

    void setFilters(List<FilterRequest> filters);

    List<SortRequest> getSorts();

    void setSorts(List<SortRequest> sorts);

    Integer getPage();

    void setPage(Integer page);

    Integer getSize();

    void setSize(Integer size);
    String getSearchBy();

    void setSearchBy(String searchBy);

    void addFilter(FilterRequest filterRequest);

    void addSort(SortRequest sortRequest);

}
