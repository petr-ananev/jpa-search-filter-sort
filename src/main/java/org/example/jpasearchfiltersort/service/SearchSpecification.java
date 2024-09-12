package com.glowbyte.decision.core.service.search;

import com.glowbyte.decision.core.enums.DisplayMode;
import com.glowbyte.decision.core.enums.ObjectType;
import com.glowbyte.decision.core.model.search.SearchRequestInterface;
import org.springframework.data.jpa.domain.Specification;

public interface SearchSpecification<T> extends Specification<T> {

    void setSearchRequest(SearchRequestInterface listSearchRequest);
    void setObjectType(ObjectType objectType);
    void setDisplayMode(DisplayMode displayMode);

}
