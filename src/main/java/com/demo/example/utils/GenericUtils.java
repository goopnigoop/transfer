package com.demo.example.utils;

import java.lang.reflect.ParameterizedType;

/**
 * The type Generic utils.
 */
public class GenericUtils {

    /**
     * Gets generic parameter class.
     *
     * @param actualClass    the actual class
     * @param parameterIndex the parameter index
     * @return the generic parameter class
     */
    public static Class getGenericParameterClass(Class actualClass, int parameterIndex) {
        return (Class) ((ParameterizedType) actualClass.getGenericSuperclass()).getActualTypeArguments()[parameterIndex];
    }
}
