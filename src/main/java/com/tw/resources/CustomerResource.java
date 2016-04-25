package com.tw.resources;

import com.tw.core.Customer;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class CustomerResource {
    private Customer customer;

    public CustomerResource(Customer customer) {

        this.customer = customer;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomer(){
        return customer;
    }
}
