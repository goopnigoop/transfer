package com.demo.example.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public final class ExceptionMessages {
    public static String getErrorMessage(ConstraintViolationException e){
        return e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).reduce((s, sN) ->String.join(",",s,sN)).orElse("ConstraintViolationException");
    }

    public static String getErrorMessage(org.hibernate.exception.ConstraintViolationException e){
        return e.getConstraintName();
    }

    public static String getErrorMessage(Exception e){
        return "Common exception";
    }
}
