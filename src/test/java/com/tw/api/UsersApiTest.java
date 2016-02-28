package com.tw.api;

import com.tw.domain.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersApiTest extends ApiTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(session.get(eq("user"))).thenReturn(TestHelper.user(1L, Role.PROJECT_MANAGER).toJson());
    }

    @Test
    public void should_import_single_user() throws Exception {
        long userId = 127263l;
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.BACKGROUND_JOB.toString());
        user.put("id", userId);
        when(session.get("user")).thenReturn(user);
        when(userRepository.createUser(any())).thenReturn(1);

        Form form = new Form();
        form.param("id", String.valueOf(userId));
        form.param("name", "Liu Yu");
        form.param("role", Role.EMPLOYEE.name());

        Response response = target("/users").request().post(Entity.form(form));

        assertThat(response.getStatus(), is(201));
        assertThat(response.getLocation().toString(), notNullValue());

    }

    @Test
    public void should_get_all_evaluations_for_your_self() throws Exception {
        long userId = 1l;
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("role", Role.PROJECT_MANAGER.toString());
        userMap.put("id", userId);
        when(session.get("user")).thenReturn(userMap);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        when(userRepository.findUserById(userId)).thenReturn(user);

        Evaluation evaluation1 = mock(Evaluation.class);
        Capability capability= mock(Capability.class);
        when(capability.getProjectId()).thenReturn(1l);
        when(evaluation1.toRefJson()).thenReturn(new HashMap<>());
        when(evaluation1.getCapability()).thenReturn(capability);
        Evaluation evaluation2 = mock(Evaluation.class);
        when(evaluation2.getCapability()).thenReturn(capability);
        when(evaluation2.toRefJson()).thenReturn(new HashMap<>());

        when(evaluationMapper.findEvaluationsBelongedToUser(userId)).thenReturn(asList(evaluation1, evaluation2));

        Response response = target("/users/"+userId +"/evaluations").request().get();

        assertThat(response.getStatus(), is(200));

        ArrayList responseBody = response.readEntity(ArrayList.class);
        assertThat(responseBody.size(), is(2));
    }

    @Ignore
    public void should_not_get_others_evaluations() throws Exception {
        long userId = 1l;
        long user2Id = 2l;
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("role", Role.EMPLOYEE.toString());
        userMap.put("id", userId);
        when(session.get("user")).thenReturn(userMap);

        User user = mock(User.class);
        when(user.getId()).thenReturn(user2Id);
        when(userRepository.findUserById(userId)).thenReturn(user);

        Evaluation evaluation1 = mock(Evaluation.class);
        when(evaluation1.toRefJson()).thenReturn(new HashMap<>());
        Evaluation evaluation2 = mock(Evaluation.class);
        when(evaluation2.toRefJson()).thenReturn(new HashMap<>());

        when(evaluationMapper.findEvaluationsBelongedToUser(userId)).thenReturn(asList(evaluation1, evaluation2));

        Response response = target("/users/"+userId +"/evaluations").request().get();

        assertThat(response.getStatus(), is(403));

    }

    @Test
    public void should_get_all_qualifications_for_your_self() throws Exception {
        long userId = 1l;
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("role", Role.EMPLOYEE.toString());
        userMap.put("id", userId);
        when(session.get("user")).thenReturn(userMap);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        when(userRepository.findUserById(userId)).thenReturn(user);

        Qualification qualification1 = mock(Qualification.class);
        when(qualification1.toRefJson()).thenReturn(new HashMap<>());
        Qualification qualification2 = mock(Qualification.class);
        when(qualification2.toRefJson()).thenReturn(new HashMap<>());

        when(qualificationMapper.findQualificationsBelongedToUser(userId)).thenReturn(asList(qualification1, qualification2));

        Response response = target("/users/"+userId +"/qualifications").request().get();

        assertThat(response.getStatus(), is(200));

        ArrayList responseBody = response.readEntity(ArrayList.class);
        assertThat(responseBody.size(), is(2));
    }

    @Ignore
    public void should_not_get_others_qualifications() throws Exception {
        long userId = 1l;
        long user2Id = 2l;
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("role", Role.EMPLOYEE.toString());
        userMap.put("id", userId);
        when(session.get("user")).thenReturn(userMap);

        User user = mock(User.class);
        when(user.getId()).thenReturn(user2Id);
        when(userRepository.findUserById(userId)).thenReturn(user);

        Qualification qualification1 = mock(Qualification.class);
        when(qualification1.toRefJson()).thenReturn(new HashMap<>());
        Qualification qualification2 = mock(Qualification.class);
        when(qualification2.toRefJson()).thenReturn(new HashMap<>());

        when(qualificationMapper.findQualificationsBelongedToUser(userId)).thenReturn(asList(qualification1, qualification2));

        Response response = target("/users/"+userId +"/qualifications").request().get();

        assertThat(response.getStatus(), is(403));

    }

    @Test
    public void should_get_users_projects() throws Exception {
        User user = TestHelper.projectManager(1L);
        when(userRepository.findUserById(eq(1L))).thenReturn(user);
        Project project1 = TestHelper.project(1L);
        Project project2 = TestHelper.project(2L);
        when(projectMapper.findUserProjects(eq(1L))).thenReturn(asList(project1, project2));
        when(serviceMapper.findServicesForProject(anyLong())).thenReturn(asList(new Service()));

        final Response response = target("/users/1/projects").request().get();
        assertThat(response.getStatus(), is(200));

        List list = response.readEntity(List.class);
        assertThat(list.size(), is(2));
    }

    @Test
    public void should_get_users_assignments() throws Exception {
        User user = TestHelper.user(1L, Role.EMPLOYEE);
        when(userRepository.findUserById(eq(1L))).thenReturn(user);

        Project project1 = TestHelper.project(1L);
        Project project2 = TestHelper.project(2L);
        Assignment assignment1 = TestHelper.assignment(1L, project1);
        Assignment assignment2 = TestHelper.assignment(1L, project2);

        when(projectMapper.findUserAssignments(eq(1L))).thenReturn(asList(assignment1, assignment2));

        final Response response = target("/users/1/assignments").request().get();
        assertThat(response.getStatus(), is(200));

        List list = response.readEntity(List.class);
        assertThat(list.size(), is(2));
    }

    @Test
    public void should_get_current_user() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("id", 1);
        user.put("name", "shanchuan");
        user.put("role", Role.EMPLOYEE.toString());
        when(session.get("user")).thenReturn(user);

        final Response response = target("/users/current").request().get();
        assertThat(response.getStatus(), is(200));
        Map map = response.readEntity(Map.class);
        assertThat(map.get("name"), is(user.get("name")));
    }
}
