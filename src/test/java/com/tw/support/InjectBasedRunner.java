package com.tw.support;

import com.google.inject.AbstractModule;
import com.google.inject.CreationException;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.google.inject.spi.Message;
import com.google.inject.util.Modules;
import com.tw.records.Models;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static org.jvnet.hk2.guice.bridge.api.GuiceBridge.getGuiceBridge;

public class InjectBasedRunner extends BlockJUnit4ClassRunner {

    private static final String SERVER_URI = "http://localhost:8888";

    protected ServiceLocator locator = Injections.createLocator();


    public InjectBasedRunner(Class<?> klass) throws InitializationError {
        super(klass);

        List<AbstractModule> modules = getAbstractModules();
        try {
            Injector injector = createInjector(Modules.override(modules).with(new TestModule()));
            bridge(locator, injector);
        } catch (CreationException ce) {
            Collection<Message> errorMessages = ce.getErrorMessages();
            errorMessages.stream().forEach(m -> System.err.println(m.getMessage()));
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        locator.inject(this);
    }

    private static void bridge(ServiceLocator serviceLocator, Injector injector) {
        getGuiceBridge().initializeGuiceBridge(serviceLocator);
        serviceLocator.getService(GuiceIntoHK2Bridge.class).bridgeGuiceInjector(injector);
    }

    private List<AbstractModule> getAbstractModules() {
        Properties properties = new Properties();
        final String connectURL = String.format(
                "%s&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull",
                System.getenv().getOrDefault("DATABASE", "jdbc:mysql://localhost:3307/music?user=mysql&password=mysql"));
        properties.setProperty("db.url", connectURL);
        List<AbstractModule> modules = new ArrayList<>(asList(new AbstractModule[]{
                new Models("development", properties),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bindConstant().annotatedWith(Names.named("server_uri")).to(SERVER_URI);
                        bind(ServiceLocator.class).toInstance(locator);
                        bind(javax.ws.rs.core.Application.class).toProvider(ApplicationProvider.class);
                        bind(ApiSupport.ClientConfigurator.class).toInstance(config -> {
                            config.register(JacksonFeature.class);
                        });
                        bind(ApiSupport.SetUp.class).toInstance(() -> {

                        });
                        bind(TestData.class).toInstance(new TestData() {
                        });
                    }
                }}));
        modules.addAll(getModules());
        return modules;
    }

    protected List<AbstractModule> getModules() {
        return asList();
    }

    @Override
    protected Object createTest() throws Exception {
        Object testClass = super.createTest();
        locator.inject(testClass);
        return testClass;
    }

    @Override
    protected String testName(FrameworkMethod method) {
        Description description = method.getAnnotation(Description.class);
        return description != null ? description.value() : super.testName(method);
    }

    private static class ApplicationProvider implements Provider<javax.ws.rs.core.Application> {
        @Inject
        Injector injector;

        @Override
        public javax.ws.rs.core.Application get() {
            ResourceConfig api = new ApiConfig();

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

        private void bridge(ServiceLocator serviceLocator, Injector injector) {
            getGuiceBridge().initializeGuiceBridge(serviceLocator);
            serviceLocator.getService(GuiceIntoHK2Bridge.class).bridgeGuiceInjector(injector);
        }
    }

    private static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
        }
    }
}
