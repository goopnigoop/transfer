package com.demo.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionUtil {

    private final static SessionFactory sessionFactory;

    private static final Logger logger = LogManager.getLogger(SessionUtil.class);

    static {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        logger.info("Session factory is created");
    }

    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private SessionUtil(){
    }

    public synchronized static Session openSession(){
        logger.info("New Session is opened");
         return getSessionFactory().openSession();
    }

}
