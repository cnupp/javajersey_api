package com.tw.api;

import com.tw.api.exception.NotFoundException;
import com.tw.api.util.BodyParser;
import com.tw.api.util.Routing;
import com.tw.domain.User;
import com.tw.mapper.UserRepository;
import com.tw.session.core.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;

@Path("/users")
public class UsersApi {
    private boolean badRequest(Map<String, Object> formParams) {
        if (formParams.get("name") == null ||
                formParams.get("id") == null) {
            return true;
        }
        return false;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(@Context Session session,
                                  @Context UserRepository userRepository,
                                  @Context UriInfo uri,
                                  Form form) {
        Map<String, Object> formParams = BodyParser.parse(form.asMap());
        if (badRequest(formParams) ||
                userRepository.createUser(formParams) != 1)
            return Response.status(400).build();

        return Response.created(Routing.user(Long.valueOf(formParams.get("id")+""))).build();
    }

    @Path("current")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response currentUser(@Context Session session) {
        return Response.status(200).entity(session.get("user")).build();
    }

    @Path("/{userId}")
    public UserApi getUser(@PathParam("userId") long userId,
                           @Context UserRepository userRepository) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException();
        }
        return new UserApi(user);
    }
}
