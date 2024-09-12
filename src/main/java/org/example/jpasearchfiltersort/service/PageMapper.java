package org.example.jpasearchfiltersort.service;

import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.model.PageDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PageMapper {

    private final ModelMapper modelMapper;

    public <T, K> PageDto<K> convertPage(Page<T> sources,
                                         Class<K> destClass) {
        return new PageDto<>(
                PageableExecutionUtils.getPage(convertList(sources.getContent(), destClass), sources.getPageable(),
                                               sources::getTotalElements));
    }

    public <T, K> List<K> convertList(List<T> sources, Class<K> destClass) {

        return sources.stream().map(x -> modelMapper.map(x, destClass)).toList();

    }

}
