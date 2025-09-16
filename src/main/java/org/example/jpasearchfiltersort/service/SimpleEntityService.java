package org.example.jpasearchfiltersort.service;

import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.dao.PageSortAndFilterDaoManager;
import org.example.jpasearchfiltersort.dto.SimpleEntityViewDto;
import org.example.jpasearchfiltersort.model.PageDto;
import org.example.jpasearchfiltersort.model.SimpleEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleEntityService {

    private final PageMapper pageMapper;

    private final PageSortAndFilterDaoManager<SimpleEntity, BasicSearchRequest> pageSortAndFilterDaoManager;

    public PageDto<SimpleEntityViewDto> getAll(BasicSearchRequest basicSearchRequest) {
        Page<SimpleEntity> complexTypes = pageSortAndFilterDaoManager.getAll(SimpleEntity.class,
                                                                             SimpleEntityViewDto.class,
                                                                             basicSearchRequest);
        return pageMapper.convertPage(complexTypes, SimpleEntityViewDto.class);
    }

}
