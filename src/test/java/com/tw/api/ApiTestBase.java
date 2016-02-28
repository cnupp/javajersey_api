package com.tw.api;

import com.tw.api.exception.NotFoundException;
import com.tw.api.filter.AuthenticationFilter;
import com.tw.domain.ProjectRepository;
import com.tw.domain.impl.ProjectRepositoryImpl;
import com.tw.mapper.*;
import com.tw.session.core.Session;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Application;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApiTestBase extends JerseyTest {
    @Mock
    Session session;

    @Mock
    SolutionRepository solutionRepository;

    @Mock
    StackRepository stackRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    EvaluationMapper evaluationMapper;

    @Mock
    QualificationMapper qualificationMapper;

    @Mock
    ProjectMapper projectMapper;

    @Mock
    CapabilityMapper capabilityMapper;

    @Mock
    ServiceMapper serviceMapper;

    @Override
    protected Application configure() {
        return new ResourceConfig().packages("com.tw.api")
                .register(NotFoundException.class)
                .register(AuthenticationFilter.class)
                .register(
                        new AbstractBinder() {
                            @Override
                            protected void configure() {
                                bind(session).to(Session.class);
                                bind(solutionRepository).to(SolutionRepository.class);
                                bind(stackRepository).to(StackRepository.class);
                                bind(userRepository).to(UserRepository.class);
                                bind(projectMapper).to(ProjectMapper.class);
                                bind(capabilityMapper).to(CapabilityMapper.class);
                                bind(evaluationMapper).to(EvaluationMapper.class);
                                bind(qualificationMapper).to(QualificationMapper.class);
                                bind(serviceMapper).to(ServiceMapper.class);
                                bind(ProjectRepositoryImpl.class).to(ProjectRepository.class);
                            }
                        }
                );
    }

    @Test
    public void test() throws Exception {
        assertThat(1, is(1));
    }
}
