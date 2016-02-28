package com.tw.api.filter;

import com.tw.domain.Role;
import com.tw.session.core.Session;
import org.glassfish.jersey.server.ExtendedUriInfo;

import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

public class AuthenticationFilter implements ContainerRequestFilter {
    @Context
    Provider<Session> sessionProvider;

    @Context
    ExtendedUriInfo extendedUriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();

        Session session = sessionProvider.get();
        Map<String, Object> currentUser = (Map<String, Object>) session.get("user");

        if (path.equals("authentication") || path.matches("projects/\\d+/users/\\d+/evaluations/\\d+/instructions")) {
            return;
        } else if (currentUser == null) {
            requestContext.abortWith(Response.status(401).build());
            return;
        }

        if (!AuthenticationTable.instance().haveAuthentication(Role.valueOf(currentUser.get("role").toString()), String.format("%s %s", method, path))) {
            requestContext.abortWith(Response.status(403).build());
        }
    }
}
