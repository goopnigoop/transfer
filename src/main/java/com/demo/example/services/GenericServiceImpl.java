package com.demo.example.services;

import com.demo.example.dao.AbstractDao;
import com.demo.example.exception.AppException;

import java.io.Serializable;
import java.util.List;

import static com.demo.example.utils.CheckUtils.checkifObjectExists;

/**
 * The type Generic service.
 *
 * @param <E> the type parameter
 * @param <K> the type parameter
 */
public class GenericServiceImpl<E, K extends Serializable> implements GenericService<E, K> {

    private AbstractDao<E,K> abstractDao;

    private SessionService sessionService;

    /**
     * Instantiates a new Generic service.
     *
     * @param abstractDao    the abstract dao
     * @param sessionService the session service
     */
    GenericServiceImpl(AbstractDao<E, K> abstractDao, SessionService sessionService) {
        this.abstractDao = abstractDao;
        this.sessionService = sessionService;
        setSessionForDao();
    }

    @Override
    public E getEntity(K id) throws AppException {
        try {
            E result = abstractDao.find(id);
            checkifObjectExists(id.toString(), result);
            return result;
        } finally {
            close();
        }
    }

    @Override
    public E saveEntity(E entity) {
        try {
            begin();
            E result = abstractDao.add(entity);
            commit();
            return result;
        } catch (Exception e) {
            rollback();
            throw e;
        } finally {
            close();
        }
    }


    @Override
    public void deleteEntity(K id) throws AppException {
        try {
            begin();
            E result = abstractDao.find(id);
            checkifObjectExists(id.toString(), result);
            abstractDao.remove(result);
            commit();
        } finally {
            close();
        }
    }

    @Override
    public List<E> getlistOfEntities() {
        List<E> accountList = abstractDao.list();
        close();
        return accountList;
    }

    private void close() {
        sessionService.close();
    }

    private void commit() {
        sessionService.commit();
    }

    private void begin() {
        sessionService.begin(abstractDao);
    }

    private void setSessionForDao() {
        sessionService.setSessionForDao(abstractDao);
    }

    private void rollback() {
        sessionService.rollback();
    }

}

