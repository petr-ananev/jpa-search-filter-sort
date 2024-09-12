package org.example.jpasearchfiltersort.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class BasicSearchRequestFactory implements SearchRequestFactory<BasicSearchRequest> {

    @Value("${paging.page-size:20}")
    private Integer pageSize;

    @Override
    public BasicSearchRequest getDefaultSearchRequestIfNull(BasicSearchRequest searchRequest) {
        if (Objects.isNull(searchRequest)) {
            searchRequest = BasicSearchRequest.builder()
                    .setPage(1)
                    .setSize(pageSize)
                    .build();
        }
        return searchRequest;
    }

}
