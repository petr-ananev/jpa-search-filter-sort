package org.example.jpasearchfiltersort.service;

public interface SearchRequestFactory<TSearchRequest extends SearchRequestInterface> {

    TSearchRequest getDefaultSearchRequestIfNull(TSearchRequest searchRequest);

}
