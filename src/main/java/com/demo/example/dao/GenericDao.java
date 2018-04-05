package com.demo.example.dao;

import java.util.List;

/**
 * The interface Generic dao.
 *
 * @param <E> the type parameter
 * @param <K> the type parameter
 */
public interface GenericDao<E,K> {
    /**
     * Add e.
     *
     * @param entity the entity
     * @return the e
     */
    E add(E entity);

    /**
     * Update.
     *
     * @param entity the entity
     */
    void update(E entity);

    /**
     * Remove.
     *
     * @param entity the entity
     */
    void remove(E entity);

    /**
     * Find e.
     *
     * @param key the key
     * @return the e
     */
    E find(K key);

    /**
     * List list.
     *
     * @return the list
     */
    List<E> list();
}
