package specs.example;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

@RunWith(ConcordionRunner.class)
public class ProjectManagerTest extends ConcordionBaseTest {

    public String importUser(String userId, String username, String email) {
        Form form = new Form();
        form.param("id", userId);
        form.param("name", username);
        form.param("email", email);
        final Response response = post("/users", cookie, form);
        return response.getStatus() + "";
    }

    public String importNewProject(String id, String name, String account) {
        Form form = new Form();
        form.param("name", name);
        form.param("id", id);
        form.param("account", account);
        final Response response = post("/projects", cookie, form);

        return response.getStatus() + "";
    }

    public String addNewMember(String userId, String start, String end, String projectId) {
        Form form = new Form();
        form.param("user_id", userId);
        form.param("starts_at", start);
        form.param("ends_at", end);
        final Response response = post("/projects/" + projectId + "/users", cookie, form);
        return response.getStatus() + "";
    }
}
