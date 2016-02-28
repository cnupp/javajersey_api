package com.tw.domain;

import com.tw.api.util.Routing;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Evaluation implements Record {
    long id;
    Capability capability;
    Timestamp createdAt;
    String commitUri;
    EvaluationResult result;

    public long userId;
    public String accessKey;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public long getId() {
        return id;
    }

    public Capability getCapability() {
        return capability;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("commit_uri", commitUri);
        return map;
    }

    @Override
    public Map<String, Object> toRefJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("uri", Routing.evaluation(userId, id));
        map.put("capability_id", capability.getId());
        map.put("created_at", createdAt);
        if (result != null) {
            map.put("status", result.getStatus());
            map.put("result", result.toJson());
        }
        map.put("stack", capability.getStack().toRefJson());
        map.put("solution", capability.getSolution().toRefJson());
        map.put("projectName", capability.getProjectName());
        map.put("project_id", capability.getProjectId());
        return map;
    }

    public String getCommitUri() {
        return commitUri;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
