package com.tw.api;

import com.tw.api.util.BodyParser;
import com.tw.api.util.Routing;
import com.tw.domain.Stack;
import com.tw.mapper.StackRepository;
import com.tw.mapper.util.PrimaryKey;
import com.tw.session.core.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Path("/stacks")
public class StacksApi {

    @POST
    public Response createNewStack(@Context Session session,
                                   @Context StackRepository stackRepo,
                                   @Context UriInfo uri,
                                   Form form) {
        Map<String, Object> stack = BodyParser.parse(form.asMap());
        if (badRequest(stack)) {
            return Response.status(400).build();
        }

        PrimaryKey primaryKey = new PrimaryKey();
        if(stackRepo.create(stack, primaryKey) != 1)
            return Response.status(400).build();
        return Response.created(Routing.stack(primaryKey.id)).build();

    }

    private boolean badRequest(Map<String, Object> stack) {
        return stack.get("name") == null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolutions(@Context Session session,
                                 @Context StackRepository stackRepository,
                                 @Context UriInfo uri) {
        List<Stack> stacks = stackRepository.getStacks();
        return Response.ok(stacks.stream()
                .map(Stack::toRefJson)
                .collect(toList())).build();
    }

    @Path("/{stackId}")
    public StackApi getStack(@PathParam("stackId") long stackId,
                             @Context StackRepository stackRepository) {
        final Stack stack = stackRepository.getStackById(stackId);
        if (stack == null) {
            throw new com.tw.api.exception.NotFoundException();
        }
        return new StackApi(stack);
    }
}
