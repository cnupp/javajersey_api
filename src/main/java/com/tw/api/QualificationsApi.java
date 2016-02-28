package com.tw.api;

import com.tw.api.util.BodyParser;
import com.tw.domain.Capability;
import com.tw.domain.Project;
import com.tw.domain.User;
import com.tw.mapper.CapabilityMapper;
import com.tw.mapper.QualificationMapper;
import com.tw.session.core.Session;

import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.Map;

public class QualificationsApi {
    private final Project project;
    private final User user;

    public QualificationsApi(Project project, User user) {

        this.project = project;
        this.user = user;
    }

    @POST
    public Response createQualification(Form form,
                                        @Context QualificationMapper qualificationMapper,
                                        @Context CapabilityMapper capabilityMapper,
                                        @Context Session session) {
        long capabilityId = Long.parseLong(form.asMap().getFirst("capability_id"));
        Capability capability = capabilityMapper.findById(project.getId(), capabilityId);
        if (capability == null) {
            return Response.status(400).build();
        }
        Map<String, Object> map = BodyParser.parse(form.asMap());
        qualificationMapper.save(user.getId(), map);
        return Response.status(200).build();
    }
}
