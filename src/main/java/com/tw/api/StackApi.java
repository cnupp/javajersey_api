package com.tw.api;

import com.tw.domain.Stack;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class StackApi {
    private Stack stack;

    public StackApi(Stack stack) {

        this.stack = stack;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStackDetail() {
        return Response.ok(stack.toJson()).build();
    }
}
