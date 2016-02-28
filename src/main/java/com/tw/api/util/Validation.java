package com.tw.api.util;

import com.tw.domain.User;

import java.util.Map;

public class Validation {
    public static boolean allExists(Map<String, Object> map, String... params) {
        for (String param : params) {
            if (!map.containsKey(param)) {
                return false;
            }
        }
        return true;
    }

    public static boolean notYourself(Map<String, Object> userMap, User user) {
        return user.getId() != Long.valueOf(userMap.get("id").toString());
    }

    public static boolean validateEvaluationResult(Map<String, Object> map) {
        return allExists(map, "status", "score") && map.get("score").toString().matches("\\d+");
    }

    public static boolean validateCreateCapability(Map<String, Object> map) {
        return allExists(map, "solution_id", "stack_id") && !(!map.get("solution_id").toString().matches("\\d+") || !map.get("stack_id").toString().matches("\\d+"));
    }
}
