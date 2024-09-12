package com.glowbyte.decision.core.service.search.rule;

import com.glowbyte.decision.core.enums.DisplayMode;
import com.glowbyte.decision.core.enums.ObjectType;
import com.glowbyte.decision.core.service.search.builder.FilterRule;
import com.glowbyte.decision.core.service.search.builder.QueryBody;
import com.glowbyte.decision.core.service.search.builder.SearchRule;
import com.glowbyte.decision.core.service.search.builder.SortRule;
import jakarta.persistence.criteria.Root;

public interface PredicateRuleService {

    String ROOT = "root";

    QueryBody registerBody(DisplayMode displayMode, Root<?> root);

    default SearchRule getSearchRule(DisplayMode displayMode) {
        return SearchRule.empty();
    }

    default SortRule getSortRule(DisplayMode displayMode) {
        return SortRule.empty();
    }

    default FilterRule getFilterRule(DisplayMode displayMode) {
        return FilterRule.empty();
    }

    ObjectType getObjectType();

}
