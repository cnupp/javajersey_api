package com.tw.session.impl;

import com.tw.session.core.SessionIdGenerator;
import com.tw.session.core.SessionStorage;
import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import java.util.HashMap;

public class SessionFactory implements Factory<com.tw.session.core.Session> {
    public static final String SESSION_IDENTIFIER = "EXAM_SESSION_ID";
    private final Provider<ContainerRequestContext> requestContextProvider;
    private final SessionStorage sessionStorage;
    private SessionIdGenerator sessionIdGenerator;
    private com.tw.session.core.Session session;
    private final Object LOCK;

    @Inject
    public SessionFactory(Provider<ContainerRequestContext> requestContextProvider,
                          SessionStorage sessionStorage,
                          SessionIdGenerator sessionIdGenerator) {
        this.requestContextProvider = requestContextProvider;
        this.sessionStorage = sessionStorage;
        this.sessionIdGenerator = sessionIdGenerator;
        LOCK = new Object();
    }

    @Override
    public com.tw.session.core.Session provide() {
        synchronized (LOCK) {
            if (session != null) {
                return session;
            }
            boolean isNew = isNew();

            String key = sessionKey();
            if (isNew) {
                sessionStorage.set(key, Session.EXPIRE_SECONDS, new HashMap<>());
                session = new Session(key, true, sessionStorage);
                return session;
            } else {
                session = new Session(key, false, sessionStorage);
                return session;
            }
        }
    }

    private String sessionKey() {
        return isNew() ? sessionIdGenerator.generate() : getSessionCookie().getValue();
    }

    private boolean isNew() {
        return getSessionCookie() == null || sessionStorage.get(getSessionCookie().getValue()) == null;
    }

    private Cookie getSessionCookie() {
        return requestContextProvider.get().getCookies().get(SESSION_IDENTIFIER);
    }

    @Override
    public void dispose(com.tw.session.core.Session instance) {

    }
}
