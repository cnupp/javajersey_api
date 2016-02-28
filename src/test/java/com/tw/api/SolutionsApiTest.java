package com.tw.api;

import com.tw.domain.Role;
import com.tw.domain.Solution;
import com.tw.domain.TestHelper;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
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

public class SolutionsApiTest extends ApiTestBase {
    @Test
    public void should_create_a_new_solution_by_ADMIN_user() throws Exception {

        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.ADMIN);
        when(session.get("user")).thenReturn(user);
        when(solutionRepository.create(any(), any())).thenReturn(1);

        Form form = new Form();
        form.param("name", "Solution 1");

        Invocation.Builder request = target("/solutions").request();

        Response response = request.post(Entity.form(form));

        assertThat(response.getStatus(), is(201));
        assertThat(response.getLocation().toString(), notNullValue());

    }

    @Test
    public void should_not_be_able_to_create_new_solution() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.EMPLOYEE);
        when(session.get("user")).thenReturn(user);
        when(solutionRepository.create(any(), any())).thenReturn(1);

        Form form = new Form();
        form.param("name", "Solution 1");

        Invocation.Builder request = target("/solutions").request();

        Response response = request.post(Entity.form(form));

        assertThat(response.getStatus(), is(403));
    }

    @Test
    public void should_get_all_solutions_by_ADMIN_user() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.ADMIN);
        when(session.get("user")).thenReturn(user);
        List<Solution> solutions = new ArrayList();
        solutions.add(TestHelper.solution(1L));
        solutions.add(TestHelper.solution(2L));

        when(solutionRepository.getSolutions()).thenReturn(solutions);

        Response response = target("/solutions").request().get();

        assertThat(response.getStatus(), is(200));

        ArrayList solutionsResponse = response.readEntity(ArrayList.class);
        assertThat(solutionsResponse.size(), is(2));

    }

    @Test
    public void should_not_get_un_existing_solution_details() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.EMPLOYEE);
        when(session.get("user")).thenReturn(user);

        long solutionId = 1l;
        when(solutionRepository.getSolutionById(solutionId)).thenReturn(null);

        Response response = target("/solutions/" + solutionId).request().get();

        assertThat(response.getStatus(), is(404));

    }

    @Test
    public void should_get_details_for_solution() throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("role", Role.EMPLOYEE);
        when(session.get("user")).thenReturn(user);
        long solutionId = 1L;

        Solution solution = TestHelper.solution(solutionId);
        when(solutionRepository.getSolutionById(solutionId)).thenReturn(solution);

        Response response = target("/solutions/" + solutionId).request().get();
        assertThat(response.getStatus(), is(200));

        Map map = response.readEntity(Map.class);
        assertThat(map.get("id"), is(1));
        assertThat(map.get("name"), is(solution.getName()));
        assertThat(map.get("description"), is(solution.getDescription()));
        assertThat(map.get("created_at"), is(solution.getCreatedAt().toString()));
    }
}
