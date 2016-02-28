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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class QualificationApiTest extends ApiTestBase {
    private Project project;
    private User user;
    private Capability capability;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        project = TestHelper.project(100L);
        when(projectMapper.findById(eq(100L))).thenReturn(project);

        user = TestHelper.user(100L, Role.EMPLOYEE);
        when(projectMapper.findUserById(eq(100L), eq(100L))).thenReturn(user);

        capability = TestHelper.capability(100L, 100L, 100L);
        when(capabilityMapper.findById(eq(100L), eq(100L))).thenReturn(capability);

        Map<String, Object> currentUser = new HashMap<>();
        currentUser.put("role", Role.PROJECT_MANAGER);
        when(session.get(eq("user"))).thenReturn(currentUser);
    }

    @Test
    public void should_create_qualification_for_user() throws Exception {
        Form form = new Form();
        form.param("capability_id", "100");

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[1];
                map.put("id", 1);
                return 1;
            }
        }).when(qualificationMapper).save(eq(100L), anyObject());

        final Response response = target("/projects/100/users/100/qualifications").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_400_with_wrong_capability_id() throws Exception {
        when(capabilityMapper.findById(eq(100L), eq(101L))).thenReturn(null);
        Form form = new Form();
        form.param("capability_id", "101");

        final Response response = target("/projects/100/users/100/qualifications").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void should_403_without_manager_create_qualification() throws Exception {
        Form form = new Form();
        form.param("capability_id", "100");

        Map<String, Object> currentUser = new HashMap<>();
        currentUser.put("role", Role.EMPLOYEE);
        when(session.get(eq("user"))).thenReturn(currentUser);

        final Response response = target("/projects/100/users/100/qualifications").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(403));
    }
}
