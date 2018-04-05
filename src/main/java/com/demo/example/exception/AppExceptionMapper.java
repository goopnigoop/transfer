package com.demo.example.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    private final Logger logger = LogManager.getLogger(AppExceptionMapper.class);

    @Override
    public Response toResponse(AppException e) {
        logger.error(e.getDeveloperMessage());
        return Response
                .status(e.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorMessage(e))
                .build();
    }
}
