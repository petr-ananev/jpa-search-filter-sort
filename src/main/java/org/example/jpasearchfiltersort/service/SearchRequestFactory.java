package com.glowbyte.decision.core.service.search;

import com.glowbyte.decision.core.model.search.SearchRequestInterface;

public interface SearchRequestFactory<TSearchRequest extends SearchRequestInterface> {

    TSearchRequest getDefaultSearchRequestIfNull(TSearchRequest searchRequest);

    TSearchRequest getDefaultHistoricalSearchRequestIfNull(TSearchRequest searchRequest);

    TSearchRequest getDefaultLifeCycleSearchRequestIfNull(TSearchRequest searchRequest);

    TSearchRequest getDefaultVersionsSearchRequestIfNull(TSearchRequest searchRequest);
}
