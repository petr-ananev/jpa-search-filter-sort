package org.example.jpasearchfiltersort.dao;

import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.model.SimpleEntity;
import org.example.jpasearchfiltersort.repository.SimpleEntityRepository;
import org.example.jpasearchfiltersort.service.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicSimpleEntityDao implements SimpleEntityDao {

    private final SimpleEntityRepository repository;

    @Override
    public Page<SimpleEntity> getAll(SearchSpecification<SimpleEntity> searchSpecification, PageRequest pageRequest) {
        return repository.findAll(searchSpecification,pageRequest);
    }

}
