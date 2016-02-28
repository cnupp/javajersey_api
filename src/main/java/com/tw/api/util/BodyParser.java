package com.tw.api.util;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BodyParser {
    public static Map<String, Object> parse(MultivaluedMap<String, String> map) {
        Map<String, Object> newMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            if (entry.getValue().size() == 1) {
                process(key, entry.getValue().get(0), newMap);
            } else {
                process(key, entry.getValue(), newMap);
            }
        }
        return newMap;
    }

    private static void process(String name, Object value, Map<String, Object> map) {
        int start = name.indexOf('[');
        int end = name.indexOf(']');

        if (start != -1 && end != -1 && end > start + 1) {
            Map<String, Object> nMap = new HashMap<>();
            final String parent = name.substring(0, start);
            if (map.containsKey(parent)) {
                nMap = (Map<String, Object>) map.get(parent);
            }
            map.put(parent, nMap);
            process(name.substring(start + 1, end) + name.substring(end + 1), value, nMap);
        } else if (start != -1 && end != -1 && end == start + 1) {
            map.put(name, new ArrayList<>());
        } else {
            map.put(name, value);
        }
    }
}