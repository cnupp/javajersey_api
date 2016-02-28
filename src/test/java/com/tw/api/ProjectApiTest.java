package com.tw.api;

import com.tw.api.util.Routing;
import com.tw.domain.Project;
import com.tw.domain.Role;
import com.tw.domain.TestHelper;
import com.tw.domain.User;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class ProjectApiTest extends ApiTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(session.get(eq("user"))).thenReturn(TestHelper.user(100L, Role.EMPLOYEE).toJson());
    }

    @Test
    public void should_import_project() throws Exception {
        Map<String, Object> backgroundJob = new HashMap<>();
        backgroundJob.put("role", Role.BACKGROUND_JOB);
        when(session.get("user")).thenReturn(backgroundJob);

        when(projectMapper.save(anyObject())).thenReturn(1);
        Form form = new Form();
        form.param("id", "123");
        form.param("name", "project");
        form.param("account", "tw");

        final Response response = target("/projects").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(201));
    }

    @Test
    public void should_400_with_wrong_project_params() throws Exception {
        Map<String, Object> backgroundJob = new HashMap<>();
        backgroundJob.put("role", Role.BACKGROUND_JOB);
        when(session.get("user")).thenReturn(backgroundJob);

        when(projectMapper.save(anyObject())).thenReturn(0);
        Form form = new Form();
        form.param("name", "project");

        final Response response = target("/projects").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void should_403_with_not_background_job_import_project() throws Exception {
        Map<String, Object> backgroundJob = new HashMap<>();
        backgroundJob.put("role", Role.EMPLOYEE);
        when(session.get("user")).thenReturn(backgroundJob);

        when(projectMapper.save(anyObject())).thenReturn(1);
        Form form = new Form();
        form.param("id", "123");
        form.param("name", "project");

        final Response response = target("/projects").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(403));
    }

    @Test
    public void should_get_one_project_by_id() throws Exception {
        Project project = TestHelper.project(100L);
        when(projectMapper.findById(eq(100L))).thenReturn(project);

        final Response response = target("/projects/100").request().get();
        assertThat(response.getStatus(), is(200));
        Map map = response.readEntity(Map.class);
        assertThat(map.get("name"), is(project.getName()));
        assertThat(map.get("account"), is(project.getAccount()));
        assertThat(map.get("capabilities_uri"), is(Routing.capabilities(project.getId()).toString()));
    }

    @Test
    public void should_404_if_project_not_found() throws Exception {
        when(projectMapper.findById(eq(100L))).thenReturn(null);

        final Response response = target("/projects/100").request().get();
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void should_assign_user_to_project() throws Exception {
        when(session.get(eq("user"))).thenReturn(TestHelper.projectManager(100L).toJson());
        final Project project = TestHelper.project(100L);
        when(projectMapper.findById(eq(100L))).thenReturn(project);
        when(projectMapper.assignUser(eq(100L), anyObject())).thenReturn(1);

        Form form = new Form();
        form.param("user_id", "100");
        form.param("starts_at", "2015-11-12");
        form.param("ends_at", "2015-12-12");
        final Response response = target("/projects/100/users").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_400_if_wrong_parameter_is_given_when_assign_user() throws Exception {
        when(session.get(eq("user"))).thenReturn(TestHelper.projectManager(100L).toJson());
        final Project project = TestHelper.project(100L);
        when(projectMapper.findById(eq(100L))).thenReturn(project);
        when(projectMapper.assignUser(eq(100L), anyObject())).thenReturn(0);

        Form form = new Form();
        form.param("abc", "100");
        final Response response = target("/projects/100/users").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void should_get_all_project_users() throws Exception {
        final Project project = TestHelper.project(100L);
        final User user1 = TestHelper.user(1L, Role.EMPLOYEE);
        final User user2 = TestHelper.user(2L, Role.EMPLOYEE);

        when(projectMapper.findById(eq(100L))).thenReturn(project);
        when(projectMapper.findUsers(eq(100L))).thenReturn(asList(user1, user2));

        final Response response = target("/projects/100/users").request().get();
        assertThat(response.getStatus(), is(200));
        List users = response.readEntity(List.class);
        assertThat(users.size(), is(2));
    }
}
