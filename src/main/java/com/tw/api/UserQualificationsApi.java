package com.tw.api;

import com.tw.domain.Qualification;
import com.tw.domain.User;
import com.tw.mapper.QualificationMapper;
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

public class UserQualificationsApi {
    private User user;

    public UserQualificationsApi(User user) {

        this.user = user;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvaluations(@Context UserRepository userRepository,
                                   @Context QualificationMapper qualificationMapper,
                                   @Context Session session){
        Map<String, Object> userMap  = (Map<String, Object>) session.get("user");
//        if (Validation.notYourself(userMap, user)) {
//            return Response.status(403).build();
//        }
        List<Qualification> qualifications = qualificationMapper.findQualificationsBelongedToUser(user.getId());

        List<Map<String, Object>> results = qualifications.stream()
                .map(qualification -> toRefJson(qualification, user))
                .collect(toList());

        return Response.ok(results).build();
    }

    private Map<String, Object> toRefJson(Qualification qualification, User user){
        Map<String, Object> result = qualification.toRefJson();
        result.put("user", user.toRefJson());
        return result;
    }
}
