package com.tw.jersey;

import com.tw.core.Customer;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class Routes {

    private final String baseUri;

    public Routes(UriInfo uriInfo) {
        baseUri = uriInfo.getBaseUri().toASCIIString();
    }

    public URI customer(Customer customer) {
        return URI.create(String.format("%scustomers/%s", baseUri, customer.getId()));
    }
}
