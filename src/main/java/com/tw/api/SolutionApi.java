package com.tw.api;

import com.tw.domain.Solution;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SolutionApi {

    private Solution solution;

    public SolutionApi(Solution solution) {

        this.solution = solution;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolutionDetail() {
        return Response.ok(solution.toJson()).build();
    }
}
