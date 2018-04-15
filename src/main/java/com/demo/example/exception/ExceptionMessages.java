package com.demo.example.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * The type Exception messages.
 */
public final class ExceptionMessages {
    /**
     * Get error message string.
     *
     * @param e the e
     * @return the string
     */
    public static String getErrorMessage(ConstraintViolationException e){
        return e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).reduce((s, sN) ->String.join(",",s,sN)).orElse("ConstraintViolationException");
    }

    /**
     * Get error message string.
     *
     * @param e the e
     * @return the string
     */
    public static String getErrorMessage(org.hibernate.exception.ConstraintViolationException e){
        return e.getCause().getMessage();
    }

    /**
     * Get error message string.
     *
     * @return the string
     */
    public static String getErrorMessage(){
        return "Common exception";
    }
}
