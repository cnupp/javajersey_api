package com.tw.session.impl;

import com.tw.session.core.SessionStorage;

import java.util.Map;
import java.util.stream.Stream;

public class Session implements com.tw.session.core.Session {
    public static final int EXPIRE_SECONDS = 1800;
    private String sessionKey;
    private SessionStorage sessionStorage;
    private boolean isNew = false;
    private boolean invalid = false;

    public Session(String sessionKey, boolean isNew, SessionStorage sessionStorage) {
        this.sessionKey = sessionKey;
        this.isNew = isNew;
        this.sessionStorage = sessionStorage;
    }

    @Override
    public Object get(String key) {
        Map<String, Object> session = (Map<String, Object>) sessionStorage.get(sessionKey);
        if (session == null) {
            return null;
        }
        sessionStorage.replace(sessionKey, EXPIRE_SECONDS, session);
        return session.get(key);
    }

    @Override
    public void set(String key, Object obj) {
        Map<String, Object> session = (Map<String, Object>) sessionStorage.get(sessionKey);
        session.put(key, obj);
        sessionStorage.replace(sessionKey, EXPIRE_SECONDS, session);
    }

    @Override
    public void delete(String key) {
        Map<String, Object> session = (Map<String, Object>) sessionStorage.get(sessionKey);
        session.remove(key);
        sessionStorage.replace(sessionKey, EXPIRE_SECONDS, session);
    }

    @Override
    public void replace(String key, Object object) {
        Map<String, Object> session = (Map<String, Object>) sessionStorage.get(sessionKey);
        session.replace(key, object);
        sessionStorage.replace(sessionKey, EXPIRE_SECONDS, session);
    }

    @Override
    public boolean isInvalid() {
        return invalid;
    }

    @Override
    public Stream<String> keys() {
        Map<String, Object> attributes = (Map<String, Object>) sessionStorage.get(sessionKey);
        sessionStorage.replace(sessionKey, EXPIRE_SECONDS, attributes);
        return attributes.keySet().stream();
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public String getSessionKey() {
        return sessionKey;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = (Map<String, Object>) sessionStorage.get(sessionKey);
        sessionStorage.replace(sessionKey, EXPIRE_SECONDS, attributes);
        return attributes;
    }

    @Override
    public void invalidate() {
        invalid = true;
        sessionStorage.delete(sessionKey);
    }
}
