package com.tw.api;

import com.tw.api.util.Routing;
import com.tw.domain.Evaluation;
import com.tw.domain.User;
import com.tw.mapper.EvaluationMapper;
import com.tw.mapper.UserRepository;
import com.tw.session.core.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class UserEvaluationsApi {
    private User user;

    public UserEvaluationsApi(User user) {

        this.user = user;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvaluations(@Context UserRepository userRepository,
                                   @Context EvaluationMapper evaluationMapper,
                                   @Context Session session) {
        Map<String, Object> userMap = (Map<String, Object>) session.get("user");

//        if (Validation.notYourself(userMap, user)) {
//            return Response.status(403).build();
//        }
        List<Evaluation> evaluations = evaluationMapper.findEvaluationsBelongedToUser(user.getId());

        List<Map<String, Object>> results = evaluations.stream()
                .map(evaluation -> toRefJson(evaluation, user))
                .collect(toList());

        return Response.ok(results).build();
    }


    private Map<String, Object> toRefJson(Evaluation evaluation, User user) {
        Map<String, Object> result = evaluation.toRefJson();
        result.put("user", user.toRefJson());
        result.put("instruction", Routing.projectEvaluation(evaluation.getCapability().getProjectId(),
                user.getId(), evaluation.getId()) + "instructions?token="+evaluation.accessKey);
        return result;
    }
}
