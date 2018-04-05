package com.demo.example.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;


/**
 * The type Abstract dao.
 *
 * @param <E> the type parameter
 * @param <K> the type parameter
 */
public abstract class AbstractDao<E, K extends Serializable> implements GenericDao<E,K> {

    protected Session currentSession;
    private Class<E> currentclass;

    /**
     * Instantiates a new Abstract dao.
     */
    AbstractDao() {
        currentclass = (Class<E>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


    @Override
    public E add(E entity) {
        currentSession.persist(entity);
        return entity;
    }

    @Override
    public void update(E entity) {
        currentSession.update(entity);
    }

    @Override
    public void remove(E entity) {
        currentSession.delete(entity);
    }

    @Override
    public E find(K key) {
        E result = (E)currentSession.get(currentclass,key);
        return result;
    }

    @Override
    public List<E> list() {
        return currentSession.createCriteria(currentclass).list();
    }

    /**
     * Gets current session.
     *
     * @return the current session
     */
    public Session getCurrentSession() {
        return currentSession;
    }

    /**
     * Sets current session.
     *
     * @param currentSession the current session
     */
    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

}
