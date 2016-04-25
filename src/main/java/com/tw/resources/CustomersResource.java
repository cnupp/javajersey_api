package com.tw.resources;

import com.tw.core.Customer;
import com.tw.core.CustomerRepository;
import com.tw.jersey.Routes;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("customers")
public class CustomersResource {
    @Inject
    CustomerRepository customerRepository;

    @Inject
    Routes routes;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(Map<String, Object> body){
        final Customer customer = customerRepository.register(body);
        return Response.created(routes.customer(customer)).build();
    }

    @Path("{customer-id}")
    public CustomerResource getCustomer(@PathParam("customer-id") String id) {
        final Customer customer = customerRepository.findById(id);
        return new CustomerResource(customer);
    }
}
