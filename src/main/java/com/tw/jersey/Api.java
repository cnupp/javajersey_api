package com.tw.jersey;

import com.tw.session.SessionFeature;
import com.tw.session.core.SessionIdGenerator;
import com.tw.session.core.SessionStorage;
import com.tw.session.impl.RandomSessionIdGenerator;
import com.tw.session.impl.RedisSessionStorage;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

import static org.glassfish.jersey.server.ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR;

public class Api extends ResourceConfig {
    public final static String RESOURCE_PACKAGE = "com.tw";

    public Api() {
        property(RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
        packages(RESOURCE_PACKAGE);
        register(RoutesFeature.class);
        register(SessionFeature.class);
        register(AuthenticationFilter.class);
        register(LoggingFilter.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new RandomSessionIdGenerator()).to(SessionIdGenerator.class);
                bind(new RedisSessionStorage("127.0.0.1")).to(SessionStorage.class);
            }
        });
        register(OpenSessionInViewRequestFilter.class);
        register(OpenSessionInViewResponseFilter.class);
    }
}
