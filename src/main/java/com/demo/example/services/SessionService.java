package com.demo.example.services;

import com.demo.example.dao.AbstractDao;
import com.demo.example.utils.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

/**
 * The type Session service.
 */
public class SessionService {

    private Session session;

    /**
     * The Logger.
     */
    private static final Logger logger = LogManager.getLogger(SessionUtil.class);

    /**
     * Instantiates a new Session service.
     */
    public SessionService() {
        this.session = SessionUtil.openSession();
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * Close session.
     */
    public void closeSession() {
        getSession().close();
    }

    /**
     * Close.
     */
    public void close() {
        if (getSession().isOpen())
        {
            closeSession();
            logger.info("Session is closed");
        }
    }

    /**
     * Commit.
     */
    public void commit() {
        getSession().getTransaction().commit();
        logger.trace("Transaction is committed");
    }

    /**
     * Begin.
     *
     * @param abstractDAO the abstract dao
     */
    public void begin(AbstractDao abstractDAO) {
        setSessionForDao(abstractDAO);
        getSession().beginTransaction();
        logger.trace("Transaction is started");
    }

    /**
     * Rollback.
     */
    public void rollback() {
        getSession().getTransaction().rollback();
        logger.trace("Transaction is rollbacked");
    }

    /**
     * Sets session for dao.
     *
     * @param abstractDAO the abstract dao
     */
    public void setSessionForDao(AbstractDao abstractDAO) {
        abstractDAO.setCurrentSession(getSession());
        logger.trace("Session is set");
    }

}
