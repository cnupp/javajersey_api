package com.tw.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class TestHelper {

    public static Project project(long id) {
        Project project = new Project();
        project.id = id;
        project.name = "project";
        project.account = "ThoughtWorks";
        return project;
    }

    public static Assignment assignment(long userId, Project project) {
        Assignment assignment = new Assignment();
        assignment.userId = userId;
        assignment.startsAt = "2015-12-12";
        assignment.endsAt = "2016-12-12";
        assignment.project = project;
        return assignment;
    }

    public static Capability capability(long id, long stackId, long solutionId) {
        Capability capability = new Capability();
        capability.id = id;
        capability.solution = solution(solutionId);
        capability.stack = stack(stackId);
        Project project = project(100L);
        capability.projectId = project.getId();
        return capability;
    }

    public static Service service(long id) {
        Service service = new Service();
        service.id = id;
        service.name = "Java";
        service.imageUrl = "http://img.stackshare.io/service/995/K85ZWV2F.png";
        return service;
    }

    public static Stack stack(long id) {
        Stack stack = new Stack();
        stack.id = id;
        stack.name = "stack";
        stack.description = "stack description";
        stack.services = asList(service(1L), service(2L));
        return stack;
    }

    public static Solution solution(long id) {
        Solution solution = new Solution();
        solution.id = id;
        solution.name = "solution";
        solution.description = "solution description";
        solution.createdAt = new Timestamp(1L);
        return solution;
    }

    public static User user(long id, Role role) {
        User user = new User();
        user.id = id;
        user.name = "user";
        user.role = role;
        return user;
    }

    public static User projectManager(long id) {
        return user(id, Role.PROJECT_MANAGER);
    }

    public static Evaluation evaluation(long evaluationId, User user, Capability capability) {
        Evaluation evaluation = new Evaluation();
        evaluation.id = evaluationId;
        evaluation.userId = user.getId();
        evaluation.capability = capability;
        evaluation.createdAt = new Timestamp(1l);
        return evaluation;
    }

    public static Map<String, Object> stackToMap(Stack stack) {
        Map<String, Object> stackMap = new HashMap<>();
        stackMap.put("name", stack.getName());
        stackMap.put("description", stack.getDescription());
        ArrayList collect = stack.getServices().stream().map(service -> service.getId()).collect(Collectors.toCollection(ArrayList::new));
        stackMap.put("services", collect);
        return stackMap;
    }

    public static Evaluation evaluation(long evaluationId, User user, Capability capability, String randomUUIDString) {
        Evaluation evaluation = evaluation(evaluationId, user, capability);
        evaluation.accessKey = randomUUIDString;
        return evaluation;
    }
}
