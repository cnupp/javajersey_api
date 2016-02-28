package com.tw.domain;

import com.tw.api.util.Routing;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class User implements Record {
    long id;
    Role role = Role.EMPLOYEE;
    String realName;
    String department;
    String primaryRole;

    public String getRealName() {
        return realName;
    }

    public String getDepartment() {
        return department;
    }

    public String getPrimaryRole() {
        return primaryRole;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    String name;

    List<Qualification> qualifications  = new ArrayList<>();

    public List<Qualification> getQualifications() {
        return qualifications;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("role", role);
        map.put("real_name", realName);
        map.put("primary_role", primaryRole);
        map.put("department", department);
        map.put("qualifications", qualifications.stream().map(Qualification::toRefJson).collect(toList()));
        return map;
    }

    @Override
    public Map<String, Object> toRefJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("uri", Routing.user(id));
        map.put("name", name);
        return map;
    }
}
