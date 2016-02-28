package com.tw.api;

import com.tw.api.util.BodyParser;
import com.tw.api.util.Routing;
import com.tw.domain.Solution;
import com.tw.mapper.SolutionRepository;
import com.tw.mapper.util.PrimaryKey;
import com.tw.session.core.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Path("/solutions")
public class SolutionsApi {
    private boolean badRequest(Map<String, Object> solutionMap) {
        return solutionMap.get("name") == null;
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewSolution(@Context Session session,
                                      @Context SolutionRepository solutionRepository,
                                      @Context UriInfo uri,
                                      Form form) {
        Map<String, Object> solutionMap = BodyParser.parse(form.asMap());

        PrimaryKey primaryKey = new PrimaryKey();
        if(badRequest(solutionMap) ||
                solutionRepository.create(solutionMap, primaryKey) != 1)
            return Response.status(400).build();
        return Response.created(Routing.solution(primaryKey.id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolutions(@Context Session session,
                                 @Context SolutionRepository solutionRepository,
                                 @Context UriInfo uri) {
        List<Solution> solutions = solutionRepository.getSolutions();
        List<Map<String, Object>> results = solutions.stream()
                .map(Solution::toRefJson)
                .collect(toList());
        return Response.ok(results).build();
    }


    @Path("/{solutionId}")
    public SolutionApi getSolution(@PathParam("solutionId") long solutionId,
                                   @Context SolutionRepository solutionRepository) {
        Solution solution = solutionRepository.getSolutionById(solutionId);
        if (solution == null) {
            throw new com.tw.api.exception.NotFoundException();
        }
        return new SolutionApi(solution);
    }
}
