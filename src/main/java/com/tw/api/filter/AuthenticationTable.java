package com.tw.api.filter;

import com.tw.domain.Role;

import java.util.*;

import static java.util.Arrays.asList;

public class AuthenticationTable {
    private static AuthenticationTable singleton;
    private Map<Role, Set<String>> table = new HashMap<>();

    static {
        singleton = new AuthenticationTable();
        singleton.addAuth(all(), "POST authentication");
        singleton.addAuth(all(), "GET users/current");

        singleton.addAuth(all(), "GET projects/\\d+/users");
        singleton.addAuth(asList(Role.ADMIN, Role.BACKGROUND_JOB, Role.PROJECT_MANAGER), "POST projects/\\d+/users");
        singleton.addAuth(asList(Role.ADMIN, Role.BACKGROUND_JOB, Role.PROJECT_MANAGER), "POST projects/\\d+/users/\\d+/qualifications");
        singleton.addAuth(asList(Role.ADMIN, Role.BACKGROUND_JOB), "POST projects");
        singleton.addAuth(asList(Role.ADMIN, Role.BACKGROUND_JOB), "POST projects/\\d+/users/\\d+/evaluations/\\d+/result");
        singleton.addAuth(asList(Role.ADMIN, Role.PROJECT_MANAGER), "POST projects/\\d+/capabilities");
        singleton.addAuth(asList(Role.ADMIN, Role.PROJECT_MANAGER), "POST projects/\\d+/capabilities/\\d+/deprecated");
        singleton.addAuth(all(), "POST projects/\\d+/users/\\d+/evaluations");
        singleton.addAuth(all(), "GET projects/\\d+/users/\\d+/qualifications");
        singleton.addAuth(all(), "GET projects/\\d+");
        singleton.addAuth(all(), "GET projects/\\d+/users/\\d+/evaluations/\\d+");
        singleton.addAuth(all(), "GET projects/\\d+/users/\\d+/evaluations/\\d+/instructions");
        singleton.addAuth(all(), "GET projects/\\d+/capabilities");

        singleton.addAuth(all(), "GET stacks/\\d+");
        singleton.addAuth(all(), "GET stacks");
        singleton.addAuth(asList(Role.ADMIN, Role.BACKGROUND_JOB), "POST services");
        singleton.addAuth(asList(Role.ADMIN, Role.BACKGROUND_JOB), "POST stacks");

        singleton.addAuth(all(), "GET solutions/\\d+");
        singleton.addAuth(all(), "GET solutions");
        singleton.addAuth(asList(Role.ADMIN, Role.BACKGROUND_JOB), "POST solutions");

        singleton.addAuth(all(), "GET users/\\d+/qualifications");
        singleton.addAuth(all(), "GET users/\\d+/evaluations");
        singleton.addAuth(all(), "GET users/\\d+/assignments");
        singleton.addAuth(all(), "GET users/\\d+/projects");
        singleton.addAuth(all(), "GET users/\\d+");

        singleton.addAuth(asList(Role.ADMIN, Role.BACKGROUND_JOB), "POST users");
    }

    private static List<Role> all() {
        return asList(Role.ADMIN, Role.BACKGROUND_JOB, Role.EMPLOYEE, Role.PROJECT_MANAGER);
    }

    private AuthenticationTable() {}

    private void addAuth(List<Role> roles, String path) {
        roles.stream().forEach((role) -> {
            if (!table.containsKey(role)) {
                table.put(role, new HashSet<>());
            }
            table.get(role).add(path);
        });
    }

    public static AuthenticationTable instance() {
        return singleton;
    }

    public boolean haveAuthentication(Role role, String methodAndPath) {
        System.out.println(methodAndPath);
        if (!table.containsKey(role)) {
            return false;
        }
        final Set<String> paths = table.get(role);
        final long matchedPath = paths.stream().filter(methodAndPath::matches).count();

        return matchedPath != 0;
    }
}
