package com.tw.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RootExceptionMapper implements ExceptionMapper<Exception> {
    static Logger logger = LoggerFactory.getLogger(RootExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        logger.error("exception happend", exception);
        exception.printStackTrace();
        return Response.status(500).build();
    }
}
