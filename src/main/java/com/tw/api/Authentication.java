package com.tw.api;

import com.tw.domain.User;
import com.tw.mapper.UserRepository;
import com.tw.session.core.Session;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

@Path("authentication")
public class Authentication {
    @POST
    public Response authenticate(@Context Session session,
                                 @Context UserRepository userRepository,
                                 Form form) {
        final String userName = form.asMap().getFirst("user_name");
        User user = userRepository.findUserByName(userName);
        if (user != null) {
            session.set("user", user.toJson());
            return Response.status(200).build();
        } else {
            return Response.status(400).build();
        }
    }

    @DELETE
    public Response unAuthenticate(@Context Session session) {
        session.delete("user");
        return Response.status(200).build();
    }
}
