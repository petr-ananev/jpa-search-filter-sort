package org.example.jpasearchfiltersort.service.rule.builder;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.FetchParent;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody.QueryBodyStageBuilder.JoinParams;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody.QueryBodyStageBuilder.QueryBodyOnlyRootBuilder;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody.QueryBodyStageBuilder.QueryBodyWithTablesBuilder;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody.QueryBodyStageBuilder.QueryBodyWithTablesWithAliasBuilder;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody.QueryBodyStageBuilder.RequireCriteriaQuery;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody.QueryBodyStageBuilder.RequireRoot;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody.QueryBodyStageBuilder.RequireTables;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody.QueryBodyStageBuilder.RequireTablesWithAlias;
import org.example.jpasearchfiltersort.service.rule.builder.QueryBody.QueryBodyStageBuilder.Table;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.criteria.JoinType.LEFT;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.example.jpasearchfiltersort.service.rule.PredicateRuleService.ROOT;

@Getter
@RequiredArgsConstructor
public class QueryBody {

    private final Map<String, FetchParent<?, ?>> queryBodyMap;

    public static RequireRoot<RequireCriteriaQuery<RequireTables<QueryBodyWithTablesBuilder>>> builder() {
        return root -> criteriaQuery -> (leftTable, rightTable) ->
                new QueryBodyWithTablesBuilder(root, criteriaQuery,
                                                 new LinkedList<>(List.of(JoinParams.of(Table.of(leftTable), Table.of(rightTable)))));
    }

    public static RequireRoot<RequireCriteriaQuery<RequireTablesWithAlias<QueryBodyWithTablesWithAliasBuilder>>> aliasBuilder() {
        return root -> criteriaQuery -> (leftTable, rightTable) ->
                new QueryBodyWithTablesWithAliasBuilder(root, criteriaQuery,
                                                        new LinkedList<>(List.of(JoinParams.of(leftTable, rightTable))));
    }

    public static RequireRoot<QueryBodyOnlyRootBuilder> onlyRoot() {
        return QueryBodyOnlyRootBuilder::new;
    }


    public class QueryBodyStageBuilder {

        @FunctionalInterface
        public interface RequireRoot<T> {

            T root(Root<?> root);

        }

        @FunctionalInterface
        public interface RequireCriteriaQuery<T> {

            T criteriaQuery(CriteriaQuery<?> criteriaQuery);

        }

        @FunctionalInterface
        public interface OptionalJoinType<T> {

            T using(JoinType joinType);

        }

        @FunctionalInterface
        public interface OptionalJoinTypeLast<T> {

            T usingInLast(JoinType joinType);

        }

        @FunctionalInterface
        public interface RequireTables<T> {

            T joinTables(String leftTable, String rightTable);

        }

        @FunctionalInterface
        public interface RequireTablesWithAlias<T> {

            T joinTables(Table leftTable, Table rightTable);

        }

        @Getter
        @AllArgsConstructor(staticName = "of")
        @RequiredArgsConstructor(staticName = "of")
        public static class Table {

            private final String name;

            private String alias;
        }

        @Getter
        @AllArgsConstructor(staticName = "of")
        @RequiredArgsConstructor(staticName = "of")
        public static class JoinParams {

            private final Table leftTable;

            private final Table rightTable;

            @Setter
            private JoinType joinType = LEFT;

        }

        @RequiredArgsConstructor
        public static class QueryBodyOnlyRootBuilder {

            private final Root<?> root;

            public QueryBody build() {
                return new QueryBody(new HashMap<>(Map.of(ROOT, root)));
            }

        }

        @RequiredArgsConstructor
        public static class QueryBodyBuilder {

            protected final Root<?> root;

            protected final CriteriaQuery<?> criteriaQuery;

            protected final LinkedList<JoinParams> joinTables;

            public QueryBody build() {
                Map<String, FetchParent<?, ?>> queryBodyMap = new HashMap<>(Map.of(ROOT, root));
                joinTables.forEach(joinTable -> joinTables(queryBodyMap, joinTable));
                return new QueryBody(queryBodyMap);
            }

            private void joinTables(Map<String, FetchParent<?, ?>> queryBodyMap,
                                    JoinParams joinParams) {
                Table leftTable = joinParams.getLeftTable();
                Table rightTable = joinParams.getRightTable();
                JoinType joinType = joinParams.getJoinType();
                From<?, ?> sourceJoin = (From<?, ?>) queryBodyMap.get(defaultIfBlank(leftTable.getAlias(), leftTable.getName()));
                if (criteriaQuery.getResultType().isAssignableFrom(Long.class)) {
                    Join<?, ?> targetJoin = sourceJoin.join(rightTable.getName(), joinType);
                    queryBodyMap.put(defaultIfBlank(rightTable.getAlias(), rightTable.getName()), targetJoin);
                } else {
                    Join<?, ?> targetJoin = (Join<?, ?>) sourceJoin.fetch(rightTable.getName(), joinType);
                    queryBodyMap.put(defaultIfBlank(rightTable.getAlias(), rightTable.getName()), targetJoin);
                }
            }

        }

        public static class QueryBodyWithTablesBuilder extends QueryBodyBuilder
                implements RequireTables<QueryBodyWithTablesBuilder> {


            public QueryBodyWithTablesBuilder(Root<?> root, CriteriaQuery<?> criteriaQuery,
                                              LinkedList<JoinParams> joinTables) {
                super(root, criteriaQuery, joinTables);
            }

            @Override
            public QueryBodyWithTablesBuilder joinTables(String leftTable, String rightTable) {
                joinTables.add(JoinParams.of(Table.of(leftTable), Table.of(rightTable)));
                return new QueryBodyWithTablesBuilder(root, criteriaQuery, joinTables);
            }

        }
        public static class QueryBodyWithTablesWithAliasBuilder extends QueryBodyBuilder
                implements RequireTablesWithAlias<QueryBodyWithTablesWithAliasBuilder> {


            public QueryBodyWithTablesWithAliasBuilder(Root<?> root, CriteriaQuery<?> criteriaQuery,
                                                       LinkedList<JoinParams> joinTables) {
                super(root, criteriaQuery, joinTables);
            }

            @Override
            public QueryBodyWithTablesWithAliasBuilder joinTables(Table leftTable, Table rightTable) {
                joinTables.add(JoinParams.of(leftTable, rightTable));
                return new QueryBodyWithTablesWithAliasBuilder(root, criteriaQuery, joinTables);
            }

        }

    }

}
