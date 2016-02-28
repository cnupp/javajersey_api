package com.tw.api;

import com.tw.api.exception.NotFoundException;
import com.tw.api.util.BodyParser;
import com.tw.api.util.Routing;
import com.tw.domain.Capability;
import com.tw.domain.Evaluation;
import com.tw.domain.Project;
import com.tw.domain.User;
import com.tw.mapper.CapabilityMapper;
import com.tw.mapper.EvaluationMapper;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;

public class EvaluationsApi {
    private Project project;
    private User user;

    public EvaluationsApi(Project project, User user) {
        this.project = project;

        this.user = user;
    }

    @POST
    public Response createEvaluation(Form form,
                                     @Context EvaluationMapper evaluationMapper,
                                     @Context CapabilityMapper capabilityMapper) {
        Map<String, Object> map = BodyParser.parse(form.asMap());
        Capability capability = capabilityMapper.findById(project.getId(), Long.parseLong(map.get("capability_id").toString()));
        map.put("accessKey", generateAccessKey());
        if (capability == null ||
                evaluationMapper.save(project.getId(), user.getId(), map) != 1) {
            return Response.status(400).build();
        }

        return Response.created(Routing.evaluation(user.getId(), Long.valueOf(map.get("id")+""))).build();
    }

    private String generateAccessKey() {
        return UUID.randomUUID().toString();
    }

    @Path("{evaluationId}")
    public EvaluationApi getEvaluationApi(@PathParam("evaluationId") long evaluationId,
                                       @Context EvaluationMapper evaluationMapper) {
        Evaluation evaluation = evaluationMapper.findById(project.getId(), user.getId(), evaluationId);
        if (evaluation == null) {
            throw new NotFoundException();
        }
        return new EvaluationApi(project, user, evaluation);
    }
}
