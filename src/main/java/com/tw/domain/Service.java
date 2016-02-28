package com.tw.domain;

import com.tw.api.util.Routing;

import java.util.HashMap;
import java.util.Map;

public class Service implements Record {
    long id;
    String name;
    String imageUrl;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("image_url", imageUrl);
        return map;
    }

    @Override
    public Map<String, Object> toRefJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("image_url", imageUrl);
        map.put("uri", Routing.service(id));
        return map;
    }
}
