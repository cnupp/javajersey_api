package com.tw.api.filter;

import com.tw.session.core.Session;
import org.glassfish.jersey.server.ExtendedUriInfo;

import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

@PreMatching
public class AuthFilter implements ContainerRequestFilter {
    @Context
    Provider<Session> sessionProvider;

    @Context
    ExtendedUriInfo extendedUriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        Session session = sessionProvider.get();
        Map<String, Object> currentUser = (Map<String, Object>) session.get("user");
        if (!path.equals("authentication") && currentUser == null) {
            requestContext.abortWith(Response.status(401).build());
        }
    }
}
