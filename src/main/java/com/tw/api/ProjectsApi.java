package com.tw.api;

import com.tw.api.util.BodyParser;
import com.tw.api.util.Routing;
import com.tw.domain.Project;
import com.tw.domain.ProjectRepository;
import com.tw.mapper.ProjectMapper;
import com.tw.session.core.Session;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("projects")
public class ProjectsApi {
    @POST
    public Response createProject(Form form,
                                  @Context ProjectMapper projectMapper,
                                  @Context ProjectRepository projectRepository,
                                  @Context Session session) {
        Map<String, Object> map = BodyParser.parse(form.asMap());

        if (projectRepository.saveProject(map) != 1)
            return Response.status(400).build();

        return Response.created(Routing.project(Long.valueOf(map.get("id")+""))).build();
    }


    @Path("{projectId}")
    public ProjectApi projectApi(@PathParam("projectId") long projectId,
                                 @Context ProjectMapper projectMapper) {
        Project project = projectMapper.findById(projectId);
        if (project == null) {
            throw new com.tw.api.exception.NotFoundException();
        }
        return new ProjectApi(project);
    }
}
