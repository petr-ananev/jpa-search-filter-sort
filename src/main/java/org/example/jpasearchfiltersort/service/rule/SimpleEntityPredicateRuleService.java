package org.example.jpasearchfiltersort.service.rule;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.example.jpasearchfiltersort.enums.ObjectType;
import org.example.jpasearchfiltersort.model.SimpleEntity_;
import org.example.jpasearchfiltersort.service.rule.builder.FilterRule;
import org.example.jpasearchfiltersort.service.rule.builder.FilterRule.FilterRuleConfig;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody;
import org.example.jpasearchfiltersort.service.rule.builder.SearchRule;
import org.example.jpasearchfiltersort.service.rule.builder.SortRule;
import org.springframework.stereotype.Service;

import java.util.List;

import static jakarta.persistence.criteria.JoinType.LEFT;
import static org.example.jpasearchfiltersort.enums.ObjectType.SIMPLE_ENTITY;
import static org.example.jpasearchfiltersort.model.SimpleEntity_.ID;
import static org.example.jpasearchfiltersort.model.SimpleEntity_.SIMPLE_RELATED_ENTITY;
import static org.example.jpasearchfiltersort.model.SimpleRelatedEntity_.OBJECT_NAME;

@Service
public class SimpleEntityPredicateRuleService implements PredicateRuleService {

    @Override
    public QueryBody registerBody(Root<?> root) {
        Join<Object, Object> join = root.join(SIMPLE_RELATED_ENTITY, LEFT);
        return QueryBody.builder()
                .bodyKey(ROOT).withPath(root)
                .bodyKey(SIMPLE_RELATED_ENTITY).withPath(join)
                .build();
    }

    @Override
    public SearchRule getSearchRule() {
        return SearchRule.builder()
                .bodyKey(ROOT)
                .withColumnSelectors(root -> List.of(root.get(ID)))
                .bodyKey(SIMPLE_RELATED_ENTITY)
                .withColumnSelectors(related -> List.of(related.get(OBJECT_NAME)))
                .build();
    }

    @Override
    public SortRule getSortRule() {
        return SortRule.builder()
                .sortColumn(SimpleEntity_.CHANGE_DT)
                .inBody(ROOT)
                .withColumnSelector(root -> root.get(SimpleEntity_.CHANGE_DT))
                .sortColumn(SimpleEntity_.CREATE_DT)
                .inBody(ROOT)
                .withColumnSelector(root -> root.get(SimpleEntity_.CREATE_DT))
                .build();
    }

    @Override
    public FilterRule getFilterRule() {
        return FilterRule.builder()
                .filterColumn(ID)
                .inBody(ROOT)
                .withColumnSelector(FilterRuleConfig.of(root -> root.get(ID), String.class))
                .filterColumn(OBJECT_NAME)
                .inBody(SIMPLE_RELATED_ENTITY)
                .withColumnSelector(FilterRuleConfig.of(related -> related.get(OBJECT_NAME), String.class))
                .build();
    }

    @Override
    public ObjectType getObjectType() {
        return SIMPLE_ENTITY;
    }

}
