package com.tw.domain;

import com.tw.api.util.Routing;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Qualification implements Record {
    long id;
    long projectId;
    long userId;

    public long getScore() {
        return score;
    }

    long score;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    Timestamp createdAt;

    public long getId() {
        return id;
    }

    public long getProjectId() {
        return projectId;
    }

    public long getUserId() {
        return userId;
    }

    public Capability getCapability() {
        return capability;
    }

    Capability capability;

    @Override
    public Map<String, Object> toJson() {
        return null;
    }

    @Override
    public Map<String, Object> toRefJson() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> capabilityMap = new HashMap<>();
        capabilityMap.put("id", capability.getId());
        map.put("id", id);
        map.put("uri", Routing.qualification(userId, id));
        map.put("created_at", createdAt);
        map.put("capability", capabilityMap);
        map.put("project_name", capability.getProjectName());
        map.put("solution_name", capability.getSolution().getName());
        map.put("stack_name", capability.getStack().getName());
        map.put("stack", capability.getStack().toJson());
        map.put("score", score);
        return map;
    }
}
