package org.example.jpasearchfiltersort.utils;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@UtilityClass
public class MapUtils {

    public static <K, V> Map<K, V> concatMap(Map<K, V> left, Map<K, V> right) {
        return Stream.concat(left.entrySet().stream(), right.entrySet().stream()).collect(
                toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2) -> k1));
    }

    @SafeVarargs
    public static <K, V> Map<K, V> concatMap(Map<K, V>... maps) {
        return Stream.of(maps).flatMap(map -> map.entrySet().stream())
                     .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2) -> k1));
    }

}
