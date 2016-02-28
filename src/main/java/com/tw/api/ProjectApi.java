package com.tw.api;

import com.tw.domain.Project;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ProjectApi {
    private Project project;

    public ProjectApi(Project project) {

        this.project = project;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProject() {
        return Response.ok().entity(project.toJson()).build();
    }

    @Path("capabilities")
    public CapabilitiesApi getCapabilitiesApi() {
        return new CapabilitiesApi(project);
    }

    @Path("users")
    public ProjectUsersApi getUsersApi() {
        return new ProjectUsersApi(project);
    }


}
