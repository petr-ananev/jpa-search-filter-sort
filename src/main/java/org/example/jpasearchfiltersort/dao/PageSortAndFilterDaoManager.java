package com.glowbyte.decision.core.dao;

import com.glowbyte.decision.core.enums.ObjectType;
import com.glowbyte.decision.core.model.search.SearchRequestInterface;
import org.springframework.data.domain.Page;

public interface DecisionPageSortAndFilterDaoManager<TEntity, TSearchRequest extends SearchRequestInterface> {

    Page<TEntity> getAllNonHistoricalLifeCycle(ObjectType objectType, TSearchRequest searchRequest);

    Page<TEntity> getAllNonHistorical(ObjectType objectType, TSearchRequest searchRequest);

    Page<TEntity> getAllHistorical(ObjectType objectType, TSearchRequest searchRequest);

    Page<TEntity> getVersions(ObjectType objectType, TSearchRequest searchRequest);

}
