package org.example.jpasearchfiltersort.service.builder;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.service.builder.FilterRule.FilterRuleStageBuilder.FinalStage;
import org.example.jpasearchfiltersort.service.builder.FilterRule.FilterRuleStageBuilder.RequireBodyKey;
import org.example.jpasearchfiltersort.service.builder.FilterRule.FilterRuleStageBuilder.RequireFilterColumn;
import org.example.jpasearchfiltersort.service.builder.FilterRule.FilterRuleStageBuilder.RequirePredicateConfig;
import org.example.jpasearchfiltersort.utils.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.emptyMap;

@Getter
@RequiredArgsConstructor
public class FilterRule {

    private final Map<String, Map<String, FilterRuleConfig>> filterRuleMap;

    public static FilterRule empty() {
        return new FilterRule(emptyMap());
    }

    public static RequireFilterColumn<RequireBodyKey<RequirePredicateConfig<FinalStage>>> builder() {
        return filterColumn -> bodyKey -> filterRuleConfig -> new FinalStage(
                new HashMap<>(Map.of(filterColumn, new HashMap<>(Map.of(bodyKey, filterRuleConfig)))));
    }

    public FilterRule merge(FilterRule filterRule) {
        return new FilterRule(MapUtils.concatMap(this.filterRuleMap, filterRule.getFilterRuleMap()));
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class FilterRuleConfig {

        private Function<From<?, ?>, Path<?>> attributePath;

        private Class<?> attributeType;


        public Expression<?> getExpression(From<?, ?> pathToPredicateApply) {
            return attributePath.apply(pathToPredicateApply).as(attributeType);
        }

    }

    public static class FilterRuleStageBuilder {

        @FunctionalInterface
        public interface RequireFilterColumn<T> {

            T filterColumn(String columnName);

        }

        @FunctionalInterface
        public interface RequireBodyKey<T> {

            T inBody(String bodyKey);

        }

        @FunctionalInterface
        public interface RequirePredicateConfig<T> {

            T withColumnSelector(FilterRuleConfig filterRuleConfig);

        }

        @RequiredArgsConstructor
        public static class FinalStage
                implements RequireFilterColumn<RequireBodyKey<RequirePredicateConfig<FinalStage>>> {

            private final Map<String, Map<String, FilterRuleConfig>> precicateMap;

            public FilterRule build() {
                return new FilterRule(precicateMap);
            }

            @Override
            public RequireBodyKey<RequirePredicateConfig<FinalStage>> filterColumn(String columnName) {
                return bodyKey -> filterRuleConfig -> {
                    if (precicateMap.containsKey(columnName)) {
                        Map<String, FilterRuleConfig> predicateConfigMap = precicateMap.get(columnName);
                        predicateConfigMap.put(bodyKey, filterRuleConfig);
                    } else {
                        precicateMap.put(columnName, new HashMap<>(Map.of(bodyKey, filterRuleConfig)));
                    }
                    return new FinalStage(new HashMap<>(precicateMap));
                };
            }

        }

    }

}
