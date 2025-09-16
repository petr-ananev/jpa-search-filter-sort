package org.example.jpasearchfiltersort.service.rule;


import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.jpasearchfiltersort.markers.DtoMarker;
import org.example.jpasearchfiltersort.markers.EntityMarker;
import org.example.jpasearchfiltersort.service.rule.builder.FilterRule;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody;
import org.example.jpasearchfiltersort.service.rule.builder.SearchRule;
import org.example.jpasearchfiltersort.service.rule.builder.SortRule;

public interface PredicateRuleService<TDto extends DtoMarker, TEntity extends EntityMarker> {

    String ROOT = "root";

    QueryBody registerBody(Root<?> root,  CriteriaQuery<?> criteriaQuery);

    default SearchRule getSearchRule() {
        return SearchRule.empty();
    }

    default SortRule getSortRule() {
        return SortRule.empty();
    }

    default FilterRule getFilterRule() {
        return FilterRule.empty();
    }

}
