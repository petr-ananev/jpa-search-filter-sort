package org.example.jpasearchfiltersort.repository;

import org.example.jpasearchfiltersort.model.SimpleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

public interface SimpleEntityRepository extends JpaRepository<SimpleEntity, String>,
                                                JpaSpecificationExecutor<SimpleEntity> {

    @Override
    @EntityGraph(value = "simple-graph")
    Page<SimpleEntity> findAll(@Nullable Specification<SimpleEntity> specification, Pageable pageable);

}
