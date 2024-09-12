package org.example.jpasearchfiltersort.service.builder;

import jakarta.persistence.criteria.From;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.service.builder.QueryBody.QueryBodyStageBuilder.FinalStage;
import org.example.jpasearchfiltersort.service.builder.QueryBody.QueryBodyStageBuilder.RequireBodyKey;
import org.example.jpasearchfiltersort.service.builder.QueryBody.QueryBodyStageBuilder.RequireBodyPath;
import org.example.jpasearchfiltersort.utils.MapUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class QueryBody {

    private final Map<String, From<?, ?>> queryBodyMap;

    public static RequireBodyKey<RequireBodyPath<FinalStage>> builder() {
        return bodyKey -> bodyPath -> new FinalStage(new HashMap<>(Map.of(bodyKey, bodyPath)));
    }

    public From<?, ?> get(String bodyKey) {
        return queryBodyMap.get(bodyKey);
    }

    public QueryBody merge(QueryBody queryBody) {
        return new QueryBody(MapUtils.concatMap(this.queryBodyMap, queryBody.getQueryBodyMap()));
    }

    public static class QueryBodyStageBuilder {


        @FunctionalInterface
        public interface RequireBodyKey<T> {

            T bodyKey(String bodyKey);

        }

        @FunctionalInterface
        public interface RequireBodyPath<T> {

            T withPath(From<?, ?> bodyPath);

        }


        @RequiredArgsConstructor
        public static class FinalStage implements RequireBodyKey<RequireBodyPath<FinalStage>> {

            private final Map<String, From<?, ?>> queryBodyMap;

            public QueryBody build() {
                return new QueryBody(queryBodyMap);
            }


            @Override
            public RequireBodyPath<FinalStage> bodyKey(String bodyKey) {
                return bodyPath -> {
                    queryBodyMap.put(bodyKey, bodyPath);
                    return new FinalStage(new HashMap<>(queryBodyMap));
                };
            }

        }

    }

}
