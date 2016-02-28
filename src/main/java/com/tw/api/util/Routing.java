package com.tw.api.util;

import java.net.URI;

import static javax.ws.rs.core.UriBuilder.fromUri;

public class Routing {
    public static URI capabilities(long projectId) {
        return fromUri("/projects/{projectId}/capabilities").build(projectId);
    }

    public static URI capability(long projectId, long capabilityId) {
        return fromUri("/projects/{projectId}/capabilities/{capabilityId}").build(projectId, capabilityId);
    }

    public static URI evaluation(long userId, long evaluationId) {
        return fromUri("/users/{userId}/evaluations/{evaluationId}").build(userId, evaluationId);
    }

    public static URI projectEvaluation(long projectId, long userId, long evaluationId) {
        return fromUri("/projects/{projectId}/users/{userId}/evaluations/{evaluationId}/").build(projectId, userId, evaluationId);
    }
    public static URI projectEvaluationResult(long projectId, long userId, long evaluationId) {
        return fromUri("/projects/{projectId}/users/{userId}/evaluations/{evaluationId}/result").build(projectId, userId, evaluationId);
    }

    public static URI solution(long solutionId) {
        return fromUri("/solutions/{solutionId}").build(solutionId);
    }

    public static URI stack(long stackId) {
        return fromUri("/stacks/{stackId}").build(stackId);
    }

    public static URI user(long userId) {
        return fromUri("/users/{userId}").build(userId);
    }

    public static URI project(long projectId) {
        return fromUri("/projects/{projectId}").build(projectId);
    }

    public static URI service(long serviceId) {
        return fromUri("/services/{serviceId}").build(serviceId);
    }

    public static URI qualification(long userId, long qualificationId) {
        return fromUri("/users/{userId}/qualifications/{qualificationId}").build(userId, qualificationId);
    }
}

