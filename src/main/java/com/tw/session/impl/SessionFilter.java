package com.tw.session.impl;

import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SessionFilter implements ContainerResponseFilter {
    @Context
    private Provider<com.tw.session.core.Session> sessionProvider;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        com.tw.session.core.Session session = this.sessionProvider.get();
        if (session.isNew()) {
            NewCookie newCookie = new NewCookie(SessionFactory.SESSION_IDENTIFIER, session.getSessionKey(), "/", "", "", -1, false, true);
            responseContext
                    .getHeaders()
                    .addFirst("Set-Cookie",
                            buildCookieHeader(newCookie));
        } else if (session.isInvalid()) {
            NewCookie deleted = new NewCookie(SessionFactory.SESSION_IDENTIFIER, "deleted", "/", "", 1, "", 0, new Date(0), false, true);
            responseContext
                    .getHeaders()
                    .addFirst("Set-Cookie",
                            buildCookieHeader(deleted));
        }
    }

    public static String buildCookieHeader(NewCookie newCookie) {
        String cookieHeader = MessageFormat.format("{0}={1};Path={2};", newCookie.getName(), newCookie.getValue(), newCookie.getPath());
        if (newCookie.getExpiry() != null) {
            cookieHeader = cookieHeader.concat("Expires=" + formatExpire(newCookie.getExpiry()) + ";");
        }
        if (newCookie.isHttpOnly()) {
            cookieHeader = cookieHeader.concat("HttpOnly;");
        }
        if (newCookie.isSecure()) {
            cookieHeader = cookieHeader.concat("Secure;");
        }
        return cookieHeader;
    }

    private static String formatExpire(Date date) {
        final SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(date);
    }
}
