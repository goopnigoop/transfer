package com.demo.example.services;

import java.util.List;

/**
 * @author aperevoz
 */
public interface GenericService<E, K> {

    public E save(E entity);

    public E get(K id);

    public void update(E entity, K id);

    public void delete(K id);

    public List<E> getList();

}
