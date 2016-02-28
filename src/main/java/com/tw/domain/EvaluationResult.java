package com.tw.domain;

import java.util.HashMap;
import java.util.Map;

public class EvaluationResult implements Record{
    long id;
    String status;
    int score;

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }


    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        map.put("score", score);
        return map;
    }

    @Override
    public Map<String, Object> toRefJson() {
        return null;
    }
}
