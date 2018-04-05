package com.demo.example.services;

import com.demo.example.dao.AbstractDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.Objects;

public class SessionUtil {

    private final static SessionFactory sessionFactory;
    private static Session session;

    static final Logger logger = LogManager.getLogger(SessionUtil.class);

    public static Session getSession() {
        return session;
    }

    public static void beginTransaction() {
        getSession().beginTransaction();
    }

    public static void commitTransaction() {
        getSession().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        getSession().getTransaction().rollback();
    }

    public static void closeSession() {
        getSession().close();
    }


    static {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        logger.info("Session factory is created");
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private SessionUtil(){

    }

    public static void openSession(){
        logger.info("New Session is opened");
        session = getSessionFactory().openSession();
    }

    public static void close() {
        if (SessionUtil.getSession().isOpen())
        {
            SessionUtil.closeSession();
            logger.info("Session is closed");
        }
    }

    public static void commit() {
        SessionUtil.commitTransaction();
        logger.trace("Transaction is committed");
    }

    public static void begin(AbstractDao abstractDAO) {
        open(abstractDAO);
        SessionUtil.beginTransaction();
        logger.trace("Transaction is started");
    }

    public static void rollback() {
        SessionUtil.rollbackTransaction();
        logger.trace("Transaction is rollbacked");
    }

    public static void open(AbstractDao abstractDAO) {
        SessionUtil.openSession();
        abstractDAO.setCurrentSession(SessionUtil.getSession());
        logger.trace("Session is set");
    }

}
