package org.example.jpasearchfiltersort.service.rule;


import jakarta.persistence.criteria.Root;
import org.example.jpasearchfiltersort.enums.ObjectType;
import org.example.jpasearchfiltersort.service.rule.builder.FilterRule;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody;
import org.example.jpasearchfiltersort.service.rule.builder.SearchRule;
import org.example.jpasearchfiltersort.service.rule.builder.SortRule;

public interface PredicateRuleService {

    String ROOT = "root";

    QueryBody registerBody(Root<?> root);

    default SearchRule getSearchRule() {
        return SearchRule.empty();
    }

    default SortRule getSortRule() {
        return SortRule.empty();
    }

    default FilterRule getFilterRule() {
        return FilterRule.empty();
    }

    ObjectType getObjectType();

}
