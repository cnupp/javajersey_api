package com.tw.api;

import com.tw.domain.Role;
import com.tw.domain.Stack;
import com.tw.domain.TestHelper;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class StacksApiTest extends ApiTestBase {
    @Test
    public void should_not_create_new_stack_by_non_project_manager_user() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.EMPLOYEE);
        when(session.get("user")).thenReturn(user);

        Form form = new Form();
        form.param("name", "Stack 1");
        form.param("description", "Stack Description");

        Response response = target("/stacks").request().post(Entity.form(form));

        assertThat(response.getStatus(), is(403));

    }

    @Test
    public void should_create_new_stack_by_admin() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.ADMIN);
        when(session.get("user")).thenReturn(user);
        when(stackRepository.create(any(), any())).thenReturn(1);
        Form form = new Form();
        form.param("name", "Stack 1");
        form.param("description", "Stack Description");
        form.param("services", "1");
        form.param("services", "2");

        Response response = target("/stacks").request().post(Entity.form(form));

        assertThat(response.getStatus(), is(201));
        assertThat(response.getLocation().toString(), notNullValue());

    }

    @Test
    public void should_get_stacks_by_project_manager() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.PROJECT_MANAGER);
        when(session.get("user")).thenReturn(user);

        List<Stack> stacks = new ArrayList();
        stacks.add(new Stack());
        stacks.add(new Stack());

        when(stackRepository.getStacks()).thenReturn(stacks);

        Response response = target("/stacks").request().get();

        assertThat(response.getStatus(), is(200));

        ArrayList solutionsResponse = response.readEntity(ArrayList.class);
        assertThat(solutionsResponse.size(), is(2));

    }

    @Test
    public void should_get_stack_details_by_all_users() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.EMPLOYEE);
        when(session.get("user")).thenReturn(user);

        long stackId = 1L;
        Stack stack = TestHelper.stack(stackId);
        when(stackRepository.getStackById(stackId)).thenReturn(stack);

        Response response = target("/stacks/" + stackId).request().get();

        assertThat(response.getStatus(), is(200));

        Map map = response.readEntity(Map.class);
        assertThat(map.get("id"), is(1));
        assertThat(map.get("name"), is(stack.getName()));
        assertThat(map.get("description"), is(stack.getDescription()));
        List services = (List) map.get("services");
        assertThat(services.size(), is(2));

    }

    @Test
    public void should_give_not_found_response_when_stack_not_exist() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.EMPLOYEE);
        when(session.get("user")).thenReturn(user);

        long stackId = 1l;
        when(stackRepository.getStackById(stackId)).thenReturn(null);

        Response response = target("/stacks/" + stackId).request().get();

        assertThat(response.getStatus(), is(404));

    }
}
