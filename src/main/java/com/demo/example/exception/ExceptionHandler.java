package com.demo.example.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintViolationException;

/**
 * The type Exception handler.
 */
public class ExceptionHandler {
    private static Logger logger = LogManager.getLogger(ExceptionHandler.class);

    /**
     * Handle app exception.
     *
     * @param e the e
     * @return the app exception
     * @throws AppException the app exception
     */
    public static AppException handle(Exception e) throws AppException {
        if(e instanceof AppException) return (AppException)e;
        if (e instanceof ConstraintViolationException) {
            logger.error(e.getMessage());
            throw new AppException(400, 401, ExceptionMessages.getErrorMessage((ConstraintViolationException) e), e.getMessage());
        } else if (e instanceof org.hibernate.exception.ConstraintViolationException) {
            logger.error(e);
            throw new AppException(500, 402, "Duplicate field in request", ExceptionMessages.getErrorMessage((org.hibernate.exception.ConstraintViolationException) e));
        } else {
            logger.error(e.getMessage());
            throw new AppException(500, 101, ExceptionMessages.getErrorMessage(e), e.toString());
        }
    }
}
