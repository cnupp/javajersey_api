package com.tw.api;

import com.tw.domain.Role;
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

public class ServiceApiTest extends ApiTestBase {
    @Test
    public void should_create_service() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.BACKGROUND_JOB.toString());
        when(session.get(eq("user"))).thenReturn(user);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[0];
                map.put("id", 1L);
                return 1;
            }
        }).when(serviceMapper).save(anyObject());

        Form form = new Form();
        form.param("name", "java");
        form.param("imageUrl", "http://test.com");

        final Response response = target("/services").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(201));
    }

    @Test
    public void should_403_if_not_background_job_role() throws Exception {

        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.EMPLOYEE.toString());
        when(session.get(eq("user"))).thenReturn(user);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[0];
                map.put("id", 1L);
                return 1;
            }
        }).when(serviceMapper).save(anyObject());

        Form form = new Form();
        form.param("name", "java");
        form.param("imageUrl", "http://test.com");

        final Response response = target("/services").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(403));
    }
}
