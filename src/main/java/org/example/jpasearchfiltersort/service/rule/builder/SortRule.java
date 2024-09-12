package org.example.jpasearchfiltersort.service.rule.builder;


import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.service.rule.builder.SortRule.SortRuleStageBuilder.FinalStage;
import org.example.jpasearchfiltersort.service.rule.builder.SortRule.SortRuleStageBuilder.RequireBodyKey;
import org.example.jpasearchfiltersort.service.rule.builder.SortRule.SortRuleStageBuilder.RequireSortColumn;
import org.example.jpasearchfiltersort.service.rule.builder.SortRule.SortRuleStageBuilder.RequireSortColumnSelector;
import org.example.jpasearchfiltersort.utils.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.emptyMap;

@Getter
@RequiredArgsConstructor
public class SortRule {

    private final Map<String, Map<String, Function<From<?, ?>, Path<?>>>> sortRuleMap;

    public static SortRule empty() {
        return new SortRule(emptyMap());
    }

    public static RequireSortColumn<RequireBodyKey<RequireSortColumnSelector<FinalStage>>> builder() {
        return sortColumn -> bodyKey -> sortColumnSelector -> new FinalStage(
                new HashMap<>(Map.of(sortColumn, new HashMap<>(Map.of(bodyKey, sortColumnSelector)))));
    }

    public SortRule merge(SortRule queryBody) {
        return new SortRule(MapUtils.concatMap(this.sortRuleMap, queryBody.getSortRuleMap()));
    }

    public static class SortRuleStageBuilder {

        @FunctionalInterface
        public interface RequireSortColumn<T> {

            T sortColumn(String columnName);

        }

        @FunctionalInterface
        public interface RequireBodyKey<T> {

            T inBody(String bodyKey);

        }

        @FunctionalInterface
        public interface RequireSortColumnSelector<T> {

            T withColumnSelector(Function<From<?, ?>, Path<?>> columnSelector);

        }

        @RequiredArgsConstructor
        public static class FinalStage
                implements RequireSortColumn<RequireBodyKey<RequireSortColumnSelector<FinalStage>>> {

            private final Map<String, Map<String, Function<From<?, ?>, Path<?>>>> sortRuleMap;

            public SortRule build() {
                return new SortRule(sortRuleMap);
            }

            @Override
            public RequireBodyKey<RequireSortColumnSelector<FinalStage>> sortColumn(String columnName) {
                return bodyKey -> columnSelector -> {
                    if (sortRuleMap.containsKey(columnName)) {
                        Map<String, Function<From<?, ?>, Path<?>>> sortRuleSelectorMap = sortRuleMap.get(columnName);
                        sortRuleSelectorMap.put(bodyKey, columnSelector);
                    } else {
                        sortRuleMap.put(columnName, new HashMap<>(Map.of(bodyKey, columnSelector)));
                    }
                    return new FinalStage(new HashMap<>(sortRuleMap));
                };
            }

        }

    }

}
