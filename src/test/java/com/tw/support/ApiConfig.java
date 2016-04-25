package com.tw.support;

import com.tw.jersey.AuthenticationFilter;
import com.tw.jersey.OpenSessionInViewRequestFilter;
import com.tw.jersey.OpenSessionInViewResponseFilter;
import com.tw.jersey.RoutesFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

import static org.glassfish.jersey.server.ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR;

public class ApiConfig extends ResourceConfig {
    public final static String RESOURCE_PACKAGE = "com.tw";

    public ApiConfig() {
        property(RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
        packages(RESOURCE_PACKAGE);
        register(RoutesFeature.class);
        register(AuthenticationFilter.class);
        register(LoggingFilter.class);
//        register(new AbstractBinder() {
//            @Override
//            protected void configure() {
//                bind(new RandomSessionIdGenerator()).to(SessionIdGenerator.class);
//                bind(new RedisSessionStorage("127.0.0.1")).to(SessionStorage.class);
//            }
//        });
        register(OpenSessionInViewRequestFilter.class);
        register(OpenSessionInViewResponseFilter.class);
    }
}
