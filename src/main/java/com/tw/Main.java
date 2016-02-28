package com.tw;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class Main {

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://0.0.0.0/").port(8088).build();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        RedisServer redisServer = new RedisServerBuilder().port(6379).build();
        redisServer.start();
        final HttpServer httpServer = startServer();

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                redisServer.stop();
                httpServer.shutdownNow();

            }
        }
    }

    private static HttpServer startServer() {
        final ResourceConfig config = ResourceConfig.forApplicationClass(ApplicationResourceConfig.class);
        return GrizzlyHttpServerFactory.createHttpServer(getBaseURI(), config);
    }

}
