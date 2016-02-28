package com.tw.domain;

import com.tw.api.util.Routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class Stack implements Record {
    long id;
    String name;
    String description;
    List<Service> services = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public List<Service> getServices() {
        return services;
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
        map.put("services", services.stream().map(Service::toRefJson).collect(toList()));
        return map;
    }

    @Override
    public Map<String, Object> toRefJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("uri", Routing.stack(id));
        map.put("description", description);
        map.put("services", services.stream().map(Service::toRefJson).collect(toList()));
        return map;
    }
}
