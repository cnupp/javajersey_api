package com.tw;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.tw.jersey.Api;
import com.tw.records.Models;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import javax.inject.Inject;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.google.inject.Guice.createInjector;
import static org.jvnet.hk2.guice.bridge.api.GuiceBridge.getGuiceBridge;

public class MainServer {
    public static void main(String[] args) throws IOException {
        RedisServer redisServer = new RedisServerBuilder()
                .port(6379)
//                .setting("maxheap 256mb")
                .build();
        redisServer.start();

        System.out.printf("Start Server...");
        WebappContext context = new WebappContext("Stacks", "/");

        ServletRegistration servletContainer = context.addServlet("ServletContainer",
                new ServletContainer((ResourceConfig) initContainer().getInstance(Application.class)));

        servletContainer.addMapping("/*");

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://0.0.0.0:8088"));
        context.deploy(server);
        server.start();

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

                server.shutdown();
            }
        }
    }

    public static Injector initContainer() {
        Properties properties = new Properties();
        String host = "localhost";
        String port = "3307";
        String username = "mysql";
        String password = "mysql";
        String connectURL = String.format(
                "jdbc:mysql://%s:%s/music?user=%s&password=%s&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull",
                host,
                port,
                username,
                password
        );
        properties.setProperty("db.url", connectURL);

        List<AbstractModule> modules = new ArrayList(Arrays.asList(
                new Models("development", properties),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(Application.class).toProvider(ApplicationProvider.class);
                    }
                }));
        return createInjector(modules);
    }

    private static class ApplicationProvider implements Provider<Application> {
        @Inject
        Injector injector;

        @Override
        public Application get() {
            Api api = new Api();

            api.register(new ContainerLifecycleListener() {
                @Override
                public void onStartup(Container container) {
                    bridge(container.getApplicationHandler().getServiceLocator(), injector);
                }

                @Override
                public void onReload(Container container) {

                }

                @Override
                public void onShutdown(Container container) {

                }
            });

            return ResourceConfig.forApplication(api);
        }

        private void bridge(ServiceLocator locator, Injector injector) {
            getGuiceBridge().initializeGuiceBridge(locator);
            locator.getService(GuiceIntoHK2Bridge.class).bridgeGuiceInjector(injector);
        }
    }
}
