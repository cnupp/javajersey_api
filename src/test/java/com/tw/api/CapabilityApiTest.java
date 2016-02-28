package com.tw.api;

import com.tw.domain.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class CapabilityApiTest extends ApiTestBase {

    private Project project;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        project = TestHelper.project(100L);
        when(projectMapper.findById(eq(100L))).thenReturn(project);

        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.PROJECT_MANAGER.toString());
        when(session.get("user")).thenReturn(user);
    }

    @Test
    public void should_create_capability_success_for_one_project() throws Exception {
        final Stack stack = TestHelper.stack(100L);
        when(stackRepository.getStackById(eq(100L))).thenReturn(stack);
        final Solution solution = TestHelper.solution(100L);
        when(solutionRepository.getSolutionById(eq(100L))).thenReturn(solution);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[1];
                map.put("id", 100L);
                return 1;
            }
        }).when(capabilityMapper).save(eq(100L), anyObject());
        Form form = new Form();
        form.param("solution_id", "100");
        form.param("stack_id", "100");
        final Response response = target("/projects/100/capabilities").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(201));
    }

    @Test
    public void should_400_with_wrong_params() throws Exception {
        final Stack stack = TestHelper.stack(100L);
        when(stackRepository.getStackById(eq(100L))).thenReturn(stack);
        final Solution solution = TestHelper.solution(100L);
        when(solutionRepository.getSolutionById(eq(100L))).thenReturn(solution);
        when(capabilityMapper.save(eq(100L), anyObject())).thenReturn(0);
        Form form = new Form();
        form.param("stack_id", "100");
        final Response response = target("/projects/100/capabilities").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void should_400_if_stack_or_solution_not_exist() throws Exception {
        when(stackRepository.getStackById(eq(100L))).thenReturn(null);
        final Solution solution = TestHelper.solution(100L);
        when(solutionRepository.getSolutionById(eq(100L))).thenReturn(solution);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[1];
                map.put("id", 100L);
                return 1;
            }
        }).when(capabilityMapper).save(eq(100L), anyObject());

        Form form = new Form();
        form.param("solution_id", "100");
        form.param("stack_id", "100");
        final Response response = target("/projects/100/capabilities").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void should_403_without_project_manager_role() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.EMPLOYEE);
        when(session.get("user")).thenReturn(user);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[0];
                map.put("id", 100L);
                return 1;
            }
        }).when(capabilityMapper).save(eq(100L), anyObject());
        Form form = new Form();
        form.param("solution_id", "100");
        form.param("stack_id", "100");
        final Response response = target("/projects/100/capabilities").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(403));
    }

    @Test
    public void should_get_all_capabilities_of_one_project() throws Exception {
        final Capability capability1 = TestHelper.capability(100L, 100L, 100L);
        final Capability capability2 = TestHelper.capability(101L, 100L, 100L);
        when(capabilityMapper.find(eq(100L), eq(false))).thenReturn(asList(capability1, capability2));

        final Response response = target("/projects/100/capabilities").request().get();
        assertThat(response.getStatus(), is(200));
        List data = response.readEntity(List.class);
        assertThat(data.size(), is(2));
    }

    @Test
    public void should_get_all_capabilities_of_one_project_including_deprecated() throws Exception {
        final Capability capability1 = TestHelper.capability(100L, 100L, 100L);
        final Capability capability2 = TestHelper.capability(101L, 100L, 100L);
        when(projectMapper.findById(eq(100L))).thenReturn(TestHelper.project(100L));
        when(capabilityMapper.find(eq(100L), eq(true))).thenReturn(asList(capability1, capability2));

        final Response response = target("/projects/100/capabilities").queryParam("all", "true").request().get();
        assertThat(response.getStatus(), is(200));
        List data = response.readEntity(List.class);
        assertThat(data.size(), is(2));
    }

    @Test
    public void should_deprecate_capabilty() throws Exception {
        final Capability capability = TestHelper.capability(100L, 100L, 100L);
        when(capabilityMapper.findById(eq(100L), eq(100L))).thenReturn(capability);

        final Response response = target("/projects/100/capabilities/100/deprecated").request().post(Entity.form(new Form()));
        assertThat(response.getStatus(), is(200));
    }
}
