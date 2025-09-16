package org.example.jpasearchfiltersort.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.ResolvableType;

import java.beans.Introspector;

@UtilityClass
@SuppressWarnings("all")
public class ClassUtils {

    public static <T> String resolveGenericTypeName(Object object, int index, Class<T> tClass) {
        return Introspector.decapitalize(ResolvableType.forClass(tClass, object.getClass())
                                                       .getGenerics()[index].resolve().getSimpleName());
    }

    public static <T> Class<?> resolveGenericType(Object object, int index, Class<T> tClass) {
        return ResolvableType.forClass(tClass, object.getClass()).getGenerics()[index].resolve();
    }

}
