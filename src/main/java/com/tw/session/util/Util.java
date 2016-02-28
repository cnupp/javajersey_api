package com.tw.session.util;

import com.tw.session.core.SessionStorage;
import com.tw.session.impl.Session;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;

public class Util {
    public static com.tw.session.core.Session getSession(ContainerRequestContext context, SessionStorage sessionStorage) {
        Cookie sessionCookie = context.getCookies().get("P2P_SESSION_ID");
        if (sessionCookie == null) {
            return null;
        }
        Object sessionValue = sessionStorage.get(sessionCookie.getValue());

        if (sessionValue == null) {
            return null;
        }

        return new Session(sessionCookie.getValue(), false, sessionStorage);
    }
}
