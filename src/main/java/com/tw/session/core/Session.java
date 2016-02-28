package com.tw.session.core;

import java.util.Map;
import java.util.stream.Stream;

public interface Session {
    Object get(String key);

    void set(String key, Object obj);

    void delete(String key);

    void replace(String key, Object object);

    boolean isInvalid();

    Stream<String> keys();

    boolean isNew();

    String getSessionKey();

    Map<String, Object> getAttributes();

    void invalidate();
}
