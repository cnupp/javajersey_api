package com.tw.resources;

import com.tw.core.CustomerRepository;
import com.tw.records.CustomerRecord;
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
                                 @Context CustomerRepository userRepository,
                                 Form form) {
        final String userName = form.asMap().getFirst("user_name");
        CustomerRecord user = (CustomerRecord) userRepository.findByName(userName);
        if (user != null) {
            session.set("user", user.toJson(null));
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
