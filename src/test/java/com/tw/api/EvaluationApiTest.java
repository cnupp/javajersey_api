package com.tw.api;

import com.tw.domain.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class EvaluationApiTest extends ApiTestBase {

    private Project project;
    private Capability capability;
    private Evaluation evaluation;
    private String randomUUIDString;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        project = TestHelper.project(100L);
        when(projectMapper.findById(eq(100L))).thenReturn(project);
        User user = TestHelper.user(100L, Role.EMPLOYEE);
        when(projectMapper.findUserById(eq(100L), eq(100L))).thenReturn(user);
        capability = TestHelper.capability(100L, 100L, 100L);
        UUID uuid = UUID.randomUUID();
        randomUUIDString = uuid.toString();
        evaluation = TestHelper.evaluation(100L, user, capability, randomUUIDString);
        when(evaluationMapper.findById(eq(100L), eq(100L), eq(100L))).thenReturn(evaluation);
        when(session.get(eq("user"))).thenReturn(TestHelper.user(100L, Role.EMPLOYEE).toJson());
    }

    @Test
    public void should_create_evaluation_for_project() throws Exception {
        final Capability capability = TestHelper.capability(100L, 100L, 100L);
        when(capabilityMapper.findById(eq(100L), eq(100L))).thenReturn(capability);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[2];
                map.put("id", 100L);
                return 1;
            }
        }).when(evaluationMapper).save(eq(100L), eq(100L), anyObject());
        Form form = new Form();
        form.param("capability_id", "100");

        final Response response = target("/projects/100/users/100/evaluations").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(201));
    }

    @Test
    public void should_400_when_create_evaluation_for_project_with_wrong_capability_id() throws Exception {
        when(capabilityMapper.findById(eq(100L), eq(100L))).thenReturn(null);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[2];
                map.put("id", 100L);
                return 1;
            }
        }).when(evaluationMapper).save(eq(100L), eq(100L), anyObject());
        Form form = new Form();
        form.param("capability_id", "100");

        final Response response = target("/projects/100/users/100/evaluations").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void should_get_evaluation() throws Exception {
        final Response response = target("/projects/100/users/100/evaluations/100").request().get();
        assertThat(response.getStatus(), is(200));
        Map map = response.readEntity(Map.class);
        assertThat(map.get("id"), is(100));
        assertThat(map.get("commit_uri"), is(evaluation.getCommitUri()));
    }

    @Test
    public void should_create_failed_evaluation_result() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.BACKGROUND_JOB);
        when(session.get("user")).thenReturn(user);

        final long userId = 100L;
        when(evaluationMapper.saveResult(eq(100L), eq(userId), eq(100L), anyObject())).thenReturn(1);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[1];
                map.put("id", 1L);
                return 1;
            }
        }).when(qualificationMapper).save(eq(userId), anyObject());

        Form form = new Form();
        form.param("status", "FAILED");
        form.param("score", "0");
        final Response response = target("/projects/100/users/100/evaluations/100/result").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_create_passed_evaluation_result() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.BACKGROUND_JOB);
        when(session.get("user")).thenReturn(user);

        final long userId = 100L;
        when(evaluationMapper.saveResult(eq(100L), eq(userId), eq(100L), anyObject())).thenReturn(1);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[1];
                map.put("id", 1L);
                return 1;
            }
        }).when(qualificationMapper).save(eq(userId), anyObject());

        Form form = new Form();
        form.param("status", "PASSED");
        form.param("score", "80");
        final Response response = target("/projects/100/users/100/evaluations/100/result").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_400_with_wrong_parameters() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.BACKGROUND_JOB);
        when(session.get("user")).thenReturn(user);

        when(evaluationMapper.saveResult(eq(100L), eq(100L), eq(100L), anyObject())).thenReturn(0);

        Form form = new Form();
        form.param("status", "PASSED");
        form.param("score", "a");
        final Response response = target("/projects/100/users/100/evaluations/100/result").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(400));

    }

    @Test
    public void should_403_without_background_job_role() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.EMPLOYEE);
        when(session.get("user")).thenReturn(user);

        when(evaluationMapper.saveResult(eq(100L), eq(100L), eq(100L), anyObject())).thenReturn(1);

        Form form = new Form();
        form.param("status", "PASSED");
        form.param("score", "80");
        final Response response = target("/projects/100/users/100/evaluations/100/result").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(403));

    }

    @Test
    public void should_be_forbidden_to_visit_evaluation_instruction_without_access_key() throws Exception {
        when(session.get(eq("user"))).thenReturn(null);
        final Response response = target("/projects/100/users/100/evaluations/100/instructions").request().get();
        assertThat(response.getStatus(), is(403));
    }

    @Test
    public void should_visit_evaluation_instruction_when_providing_right_access_key() throws Exception {
        when(session.get(eq("user"))).thenReturn(null);
        final Response response = target("/projects/100/users/100/evaluations/100/instructions").queryParam("token", randomUUIDString).request().get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_be_forbidden_to_visit_evaluation_instruction_with_wrong_access_key() throws Exception {
        when(session.get(eq("user"))).thenReturn(null);
        final Response response = target("/projects/100/users/100/evaluations/100/instructions").queryParam("token", UUID.randomUUID()).request().get();
        assertThat(response.getStatus(), is(403));
    }
}
