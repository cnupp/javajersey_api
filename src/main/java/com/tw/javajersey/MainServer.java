package com.tw.javajersey;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class MainServer {

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://0.0.0.0/").port(8088).build();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final HttpServer httpServer = startServer();

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                httpServer.shutdownNow();
            }
        }
    }

    private static HttpServer startServer() {


        final ResourceConfig config = new ResourceConfig()
                .packages("com.tw.javajersey")
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {

                    }
                });
        return GrizzlyHttpServerFactory.createHttpServer(getBaseURI(), config);
    }

}
