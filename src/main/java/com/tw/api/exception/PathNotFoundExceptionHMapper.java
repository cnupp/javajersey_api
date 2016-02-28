package com.tw.api.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class PathNotFoundExceptionHMapper implements ExceptionMapper<javax.ws.rs.NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(404).build();
    }
}
