package com.demo.example.services;

import com.demo.example.exception.AppException;

import java.io.Serializable;
import java.util.List;

/**
 * The interface Generic service.
 *
 * @param <E> the type parameter
 * @param <K> the type parameter
 * @author aperevoz
 */
public interface GenericService<E, K extends Serializable> {

    /**
     * Gets entity.
     *
     * @param id the id
     * @return the entity
     * @throws AppException the app exception
     */
    E getEntity(K id) throws AppException;

    /**
     * Delete entity.
     *
     * @param id the id
     * @throws AppException the app exception
     */
    void deleteEntity(K id) throws AppException;

    /**
     * Gets of entities.
     *
     * @return the of entities
     */
    List<E> getlistOfEntities();

    /**
     * Save entity e.
     *
     * @param entity the entity
     * @return the e
     */
    E saveEntity(E entity);
}
