package com.tw.domain;

import java.util.HashMap;
import java.util.Map;

public class Assignment implements Record {
    long id;
    long userId;
    String startsAt;
    String endsAt;
    Project project;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public String getEndsAt() {
        return endsAt;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("id", project.getId());
        map.put("name", project.getName());
        map.put("account", project.getAccount());
        map.put("starts_at", startsAt);
        map.put("ends_at", endsAt);
        return map;
    }

    @Override
    public Map<String, Object> toRefJson() {
        return toJson();
    }

    public Project getProject() {
        return project;
    }
}
