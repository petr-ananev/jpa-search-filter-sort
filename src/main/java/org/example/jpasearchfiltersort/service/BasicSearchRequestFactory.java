package com.glowbyte.decision.core.service.search;

import com.glowbyte.decision.core.model.search.ListSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.glowbyte.decision.core.service.search.SearchRequestUtils.addLifeCycleStateFilter;
import static com.glowbyte.decision.core.service.search.SearchRequestUtils.addVersionTypeFilter;
import static com.glowbyte.decision.core.service.search.SearchRequestUtils.addVersionsTypeFilter;

@Service
@RequiredArgsConstructor
public class ListSearchRequestFactory implements SearchRequestFactory<ListSearchRequest> {
    @Value("${decision-backend.paging.page-size:20}")
    private Integer pageSize;

    @Override
    public ListSearchRequest getDefaultSearchRequestIfNull(ListSearchRequest searchRequest) {
        if (Objects.isNull(searchRequest)) {
            searchRequest = ListSearchRequest.builder()
                    .setPage(1)
                    .setSize(pageSize)
                    .build();
        }
        return searchRequest;
    }

    @Override
    public ListSearchRequest getDefaultHistoricalSearchRequestIfNull(ListSearchRequest searchRequest) {
        ListSearchRequest histSearchRequest = getDefaultSearchRequestIfNull(searchRequest);
        //Для историчных сущностей необходимо добавить фильтрацию по LATEST версии
        addVersionTypeFilter(histSearchRequest);
        addLifeCycleStateFilter(histSearchRequest);
        return histSearchRequest;
    }

    @Override
    public ListSearchRequest getDefaultLifeCycleSearchRequestIfNull(ListSearchRequest listSearchRequest) {
        ListSearchRequest lifeCycleSearchRequest = getDefaultSearchRequestIfNull(listSearchRequest);
        addLifeCycleStateFilter(lifeCycleSearchRequest);
        return lifeCycleSearchRequest;
    }

    @Override
    public ListSearchRequest getDefaultVersionsSearchRequestIfNull(ListSearchRequest searchRequest) {
        ListSearchRequest histSearchRequest = getDefaultSearchRequestIfNull(searchRequest);
        addLifeCycleStateFilter(histSearchRequest);
        addVersionsTypeFilter(histSearchRequest);
        return histSearchRequest;
    }

}
