package com.tw.api;

import com.tw.domain.*;
import com.tw.mapper.ProjectMapper;
import com.tw.mapper.ServiceMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class UserApi {
    private User user;

    public UserApi(User user) {

        this.user = user;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        return Response.ok().entity(user.toJson()).build();
    }

    @Path("/evaluations")
    public UserEvaluationsApi getEvaluations() {
        return new UserEvaluationsApi(user);
    }

    @Path("/qualifications")
    public UserQualificationsApi getQualification() {
        return new UserQualificationsApi(user);
    }

    @Path("projects")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjects(@Context ProjectMapper projectMapper,
                                @Context ServiceMapper serviceMapper) {
        List<Project> projectList = projectMapper.findUserProjects(user.getId());
        List projectServices = new ArrayList<>();
        projectList.stream().forEach(project -> {
            List<Service> services = serviceMapper.findServicesForProject(project.getId());
            List<Map<String, Object>> serviceCollect = services.stream().map(Service::toJson).collect(toList());
            projectServices.add(serviceCollect);
        });

        return Response.status(200).entity(toRefJsonList(projectList, projectServices)).build();
    }

    @Path("assignments")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssignments(@Context ProjectMapper projectMapper) {
        final List<Assignment> assignments = projectMapper.findUserAssignments(user.getId());
        return Response.status(200).entity(assignments.stream().map(Assignment::toRefJson).collect(toList())).build();
    }

    private List toRefJsonList(List<Project> projectList, List projectServices) {
        return projectList.stream().map(project -> {
            Map<String, Object> stringObjectMap = project.toRefJson();
            stringObjectMap.put("services", projectServices.get(projectList.indexOf(project)));
            return stringObjectMap;
        }).collect(toList());
    }
}
