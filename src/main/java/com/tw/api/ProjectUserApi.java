package com.tw.api;

import com.tw.domain.Project;
import com.tw.domain.User;

import javax.ws.rs.Path;

public class ProjectUserApi {
    private Project project;
    private User user;

    public ProjectUserApi(Project project, User user) {
        this.project = project;

        this.user = user;
    }

    @Path("evaluations")
    public EvaluationsApi getEvaluationsApi() {
        return new EvaluationsApi(project, user);
    }

    @Path("qualifications")
    public QualificationsApi getQualificationsApi() {
        return new QualificationsApi(project, user);
    }
}
