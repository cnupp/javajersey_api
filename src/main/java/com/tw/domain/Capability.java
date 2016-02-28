package com.tw.domain;

import com.tw.api.util.Routing;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Capability implements Record {
    long id;
    Solution solution;
    Stack stack;
    Timestamp createdAt;
    long projectId;
    String projectName;
    Timestamp deprecatedAt;

    public String getProjectName() {
        return projectName;
    }

    public long getProjectId() {
        return projectId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public long getId() {
        return id;
    }

    public Solution getSolution() {
        return solution;
    }

    public Stack getStack() {
        return stack;
    }

    @Override
    public Map<String, Object> toJson() {
        Map <String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("created_at", createdAt);
        map.put("deprecated_at", deprecatedAt);
        map.put("stack", stack.toRefJson());
        map.put("solution", solution.toRefJson());
        return map;
    }

    @Override
    public Map<String, Object> toRefJson() {
        Map <String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("created_at", createdAt);
        map.put("stack", stack.toRefJson());
        map.put("solution", solution.toRefJson());
        map.put("deprecated_at", deprecatedAt);
        map.put("uri", Routing.capability(projectId, id));
        return map;
    }

    public Timestamp getDeprecatedAt() {
        return deprecatedAt;
    }
}
