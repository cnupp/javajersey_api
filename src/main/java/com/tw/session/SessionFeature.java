package com.tw.session;

import com.tw.session.core.Session;
import com.tw.session.impl.SessionFactory;
import com.tw.session.impl.SessionFilter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class SessionFeature implements Feature {
    @Override
    public boolean configure(FeatureContext context) {
        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(SessionFactory.class, RequestScoped.class).to(Session.class).in(RequestScoped.class);
            }
        });

        context.register(SessionFilter.class);

        return true;
    }
}
