package com.tw.api;

import com.tw.api.util.BodyParser;
import com.tw.api.util.Routing;
import com.tw.mapper.ServiceMapper;
import com.tw.session.core.Session;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("services")
public class ServicesApi {
    @POST
    public Response createService(Form form,
                                  @Context ServiceMapper serviceMapper,
                                  @Context Session session) {
        Map<String, Object> serviceMap = BodyParser.parse(form.asMap());
        if (serviceMapper.save(serviceMap) != 1) {
            return Response.status(400).build();
        }
        return Response.created(Routing.service(Long.valueOf(serviceMap.get("id") + ""))).build();
    }
}
