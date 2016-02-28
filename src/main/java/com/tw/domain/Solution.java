package com.tw.domain;

import com.tw.api.util.Routing;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Solution implements Record {
    long id;
    String name;
    Timestamp createdAt;
    String description;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("description", description);
        map.put("created_at", createdAt.toString());
        return map;
    }

    @Override
    public Map<String, Object> toRefJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("uri", Routing.solution(id));
        map.put("description", description);
        map.put("created_at", createdAt.toString());
        return map;
    }

    public String getDescription() {
        return description;
    }
}
