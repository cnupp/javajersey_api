package com.tw.session.core;

public interface SessionStorage {
    public boolean set(String key, int expireSeconds, Object obj);

    public Object get(String key);

    public boolean delete(String key);

    public boolean replace(String key, int expireSeconds, Object obj);
}

