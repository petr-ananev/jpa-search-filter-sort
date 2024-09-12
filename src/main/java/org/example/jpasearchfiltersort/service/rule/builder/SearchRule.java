package org.example.jpasearchfiltersort.service.builder;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.service.builder.SearchRule.SearchRuleStageBuilder.FinalStage;
import org.example.jpasearchfiltersort.service.builder.SearchRule.SearchRuleStageBuilder.RequireBodyKey;
import org.example.jpasearchfiltersort.service.builder.SearchRule.SearchRuleStageBuilder.RequireSortColumnSelector;
import org.example.jpasearchfiltersort.utils.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.emptyMap;

@Getter
@RequiredArgsConstructor
public class SearchRule {

    private final Map<String, Function<From<?, ?>, List<Path<String>>>> searchRuleMap;

    public static SearchRule empty() {
        return new SearchRule(emptyMap());
    }

    public static RequireBodyKey<RequireSortColumnSelector<FinalStage>> builder() {
        return bodyKey -> columnSelector -> new FinalStage(new HashMap<>(Map.of(bodyKey, columnSelector)));
    }

    public SearchRule merge(SearchRule queryBody) {
        return new SearchRule(MapUtils.concatMap(this.searchRuleMap, queryBody.getSearchRuleMap()));
    }

    public static class SearchRuleStageBuilder {

        @FunctionalInterface
        public interface RequireBodyKey<T> {

            T bodyKey(String bodyKey);

        }

        @FunctionalInterface
        public interface RequireSortColumnSelector<T> {

            T withColumnSelectors(Function<From<?, ?>, List<Path<String>>> columnSelector);

        }

        @RequiredArgsConstructor
        public static class FinalStage implements RequireBodyKey<RequireSortColumnSelector<FinalStage>> {

            private final Map<String, Function<From<?, ?>, List<Path<String>>>> searchRuleMap;

            public SearchRule build() {
                return new SearchRule(searchRuleMap);
            }

            @Override
            public RequireSortColumnSelector<FinalStage> bodyKey(String bodyKey) {
                return columnSelector -> {
                    searchRuleMap.put(bodyKey, columnSelector);
                    return new FinalStage(new HashMap<>(searchRuleMap));
                };
            }

        }

    }

}
