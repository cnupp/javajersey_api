package com.tw.domain;

import com.tw.api.util.Routing;

import java.util.HashMap;
import java.util.Map;

public class Project implements Record {
    long id;
    String name;
    String account;

    public String getAccount() {
        return account;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("account", account);
        map.put("capabilities_uri", Routing.capabilities(id));
        return map;
    }

    @Override
    public Map<String, Object> toRefJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("account", account);
        map.put("capabilities_uri", Routing.capabilities(id));
        map.put("uri", Routing.project(id));
        return map;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
