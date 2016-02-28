package com.tw.api;

import com.tw.api.util.BodyParser;
import com.tw.api.util.Routing;
import com.tw.api.util.Validation;
import com.tw.domain.*;
import com.tw.mapper.CapabilityMapper;
import com.tw.mapper.SolutionRepository;
import com.tw.mapper.StackRepository;
import com.tw.session.core.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class CapabilitiesApi {
    private Project project;

    public CapabilitiesApi(Project project) {

        this.project = project;
    }

    @POST
    public Response createCapability(Form form,
                                     @Context CapabilityMapper capabilityMapper,
                                     @Context StackRepository stackRepository,
                                     @Context SolutionRepository solutionRepository,
                                     @Context Session session) {
        Map<String, Object> map = BodyParser.parse(form.asMap());
        if (!Validation.validateCreateCapability(map)) {
            return Response.status(400).build();
        }
        Stack stack = stackRepository.getStackById(Long.parseLong(map.get("stack_id").toString()));
        Solution solution = solutionRepository.getSolutionById(Long.parseLong(map.get("solution_id").toString()));
        if (stack == null || solution == null) {
            return Response.status(400).build();
        }
        if (capabilityMapper.save(project.getId(), map) != 1)
            return Response.status(400).build();

        return Response.created(Routing.capability(project.getId(), Long.valueOf(map.get("id")+""))).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCapabilities(@Context CapabilityMapper capabilityMapper, @QueryParam("all") @DefaultValue("false") boolean all) {
        return Response.status(200).entity(
                capabilityMapper.find(
                        project.getId(), all).stream().map(Capability::toRefJson).collect(toList())).build();
    }

    @Path("{capabilityId}")
    public CapabilityApi getCapability(@PathParam("capabilityId") long capabilityId,
                                  @Context CapabilityMapper capabilityMapper) {
        Capability capability = capabilityMapper.findById(project.getId(), capabilityId);
        if (capability == null) {
            throw new com.tw.api.exception.NotFoundException();
        }
        return new CapabilityApi(capability);
    }
}
