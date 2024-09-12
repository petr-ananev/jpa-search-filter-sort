package org.example.jpasearchfiltersort.model;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Объект обертка для отображении страницы на клиенте.
 * <p>
 * Состоит из:
 * Общее кол-во страниц {@link #getTotalPages()}
 * Общее кол-во элементов (LIKE,EQUAL etc) {@link #getTotalElements()}
 * Текущая страница {@link #getCurrentPageNumber()}
 * Контент страницы {@link #getContent()}
 * </p>
 *
 * @param <T> - сущность (Diagram, Deploy etc)
 */
@RequiredArgsConstructor
public class Page<T> {

    private final org.springframework.data.domain.Page<T> page;

    public int getCurrentPageNumber() {
        return page.getNumber();
    }

    public int getTotalPages() {
        return page.getTotalPages();
    }

    public long getTotalElements() {
        return page.getTotalElements();
    }

    public List<T> getContent() {
        return page.getContent();
    }

}
