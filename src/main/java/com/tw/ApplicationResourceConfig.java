package com.tw;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tw.api.exception.NotFoundExceptionMapper;
import com.tw.api.exception.PathNotFoundExceptionHMapper;
import com.tw.session.SessionFeature;
import com.tw.session.core.SessionIdGenerator;
import com.tw.session.core.SessionStorage;
import com.tw.session.impl.RandomSessionIdGenerator;
import com.tw.session.impl.RedisSessionStorage;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;

import static org.jvnet.hk2.guice.bridge.api.GuiceBridge.getGuiceBridge;

public class ApplicationResourceConfig extends ResourceConfig {
    @Inject
    public ApplicationResourceConfig(ServiceLocator serviceLocator) {
        bridge(serviceLocator, Guice.createInjector(new Models("default"), new AbstractModule() {
            @Override
            protected void configure() {
                bind(ServiceLocator.class).toInstance(serviceLocator);
            }
        }));
        packages("com.tw.api");
        register(SessionFeature.class);
        register(NotFoundExceptionMapper.class);
        register(PathNotFoundExceptionHMapper.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new RandomSessionIdGenerator()).to(SessionIdGenerator.class);
                bind(new RedisSessionStorage("127.0.0.1")).to(SessionStorage.class);
            }
        });
    }

    private void bridge(ServiceLocator serviceLocator, Injector injector) {
        getGuiceBridge().initializeGuiceBridge(serviceLocator);
        serviceLocator.getService(GuiceIntoHK2Bridge.class).bridgeGuiceInjector(injector);
    }
}
