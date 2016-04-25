package com.tw.jersey;

import com.tw.session.core.Session;
import org.glassfish.jersey.server.ExtendedUriInfo;

import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationFilter implements ContainerRequestFilter {
    @Context
    Provider<Session> sessionProvider;

    @Context
    ExtendedUriInfo extendedUriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        Session session = sessionProvider.get();
        Map<String, Object> currentUser = (Map<String, Object>) session.get("user");

        String resourceOwnerId = findFirst("customers/([^/]*)", path);
        if (path.equals("authentication") || path.equals("customers") || path.contains("musics")) {
            return;
        } else if (currentUser == null) {
            requestContext.abortWith(Response.status(401).build());
            return;
        }

        if (!currentUser.get("id").equals(resourceOwnerId)) {
            requestContext.abortWith(Response.status(403).build());
        }
    }

    private String findFirst(String reg, String source) {
        String result = "";
        Pattern patt = Pattern.compile(reg);
        Matcher mat = patt.matcher(source);

        if(mat.find()) {
            result = mat.group(1);
        }
        return result;
    }
}
