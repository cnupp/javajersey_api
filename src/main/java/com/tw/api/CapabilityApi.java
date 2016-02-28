package com.tw.api;

import com.tw.domain.Capability;
import com.tw.mapper.CapabilityMapper;
import com.tw.session.core.Session;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CapabilityApi {

    private Capability capability;

    public CapabilityApi(Capability capability) {

        this.capability = capability;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCapability() {
        return Response.status(200).entity(capability.toJson()).build();
    }

    @POST
    @Path("deprecated")
    public Response deprecateCapability(@Context CapabilityMapper capabilityMapper,
                                        @Context Session session) {
        capabilityMapper.deprecate(capability);
        return Response.status(200).build();
    }
}
