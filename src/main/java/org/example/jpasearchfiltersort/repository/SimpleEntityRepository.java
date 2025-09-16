package org.example.jpasearchfiltersort.repository;

import org.example.jpasearchfiltersort.model.SimpleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SimpleEntityRepository extends JpaRepository<SimpleEntity, String>,
                                                JpaSpecificationExecutor<SimpleEntity> {

}
