package com.tw.api;

import com.tw.api.exception.NotFoundException;
import com.tw.api.util.BodyParser;
import com.tw.domain.Evaluation;
import com.tw.domain.Project;
import com.tw.domain.Qualification;
import com.tw.domain.User;
import com.tw.mapper.EvaluationMapper;
import com.tw.mapper.ProjectMapper;
import com.tw.mapper.QualificationMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ProjectUsersApi {
    private Project project;

    public ProjectUsersApi(Project project) {

        this.project = project;
    }

    @POST
    public Response assignUser(Form form,
                               @Context ProjectMapper projectMapper) {
        Map<String, Object> map = BodyParser.parse(form.asMap());
        final int result = projectMapper.assignUser(project.getId(), map);
        if (result != 1)
            return Response.status(400).build();

        return Response.status(200).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectUsers(@Context ProjectMapper projectMapper,
                                    @Context QualificationMapper qualificationMapper,
                                    @Context EvaluationMapper evaluationMapper) {
        final List<User> projectUsers = projectMapper.findUsers(project.getId());
        List<Map<String, Object>> result = projectUsers.stream().map((user) -> {
            Map<String, Object> userMap = user.toRefJson();
            userMap.put("evaluations", evaluationMapper.findEvaluationsBelongedToProjectUser(project.getId(), user.getId()).stream().map(Evaluation::toRefJson).collect(toList()));
            userMap.put("qualifications", qualificationMapper.findQualificationsBelongToUserOfProject(project.getId(), user.getId()).stream().map(Qualification::toRefJson).collect(toList()));
            return userMap;
        }).collect(toList());
        return Response.status(200).entity(result).build();
    }

    @Path("{userId}")
    public ProjectUserApi getUserApi(@PathParam("userId") long userId,
                                     @Context ProjectMapper projectMapper) {
        User user = projectMapper.findUserById(project.getId(), userId);
        if (user == null) {
            throw new NotFoundException();
        }
        return new ProjectUserApi(project, user);
    }
}
