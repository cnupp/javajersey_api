package com.tw.resources.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalExceptionMapper implements ExceptionMapper<InternalException> {

    @Override
    public Response toResponse(InternalException exception) {
        return Response.status(500).entity(exception.getMessage()).build();
    }
}
