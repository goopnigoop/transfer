package com.demo.example;


/**
 * The interface Dto mappable.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 * @author aperevoz
 */
public interface DtoMappable<T, R> {

    /**
     * Gets dto.
     *
     * @param t the t
     * @return the dto
     */
    R getDto(T t);

    /**
     * Gets by dto.
     *
     * @param r the r
     * @return the by dto
     */
    T getByDto(R r);
}
