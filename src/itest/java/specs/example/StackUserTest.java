package specs.example;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(ConcordionRunner.class)
public class StackUserTest extends ConcordionBaseTest{
    String userId="123123";
    String username="scxu";
    String email="scxu@thoughtworks.com";

    public String importUser() {
        Form form = new Form();
        form.param("id", userId);
        form.param("name", username);
        form.param("email", email);
        final Response response = post("/users", cookie, form);
        return response.getStatus() + "";
    }

    private String importNewProject(String id, String name, String account) {
        Form form = new Form();
        form.param("name", name);
        form.param("id", id);
        form.param("account", account);
        final Response response = post("/projects", cookie, form);

        return response.getStatus() + "";
    }

    public void assignUserToProject(){
        final String projectId = "1024";
        importNewProject(projectId, "CMS", "ThoughtWorks");

        Form form = new Form();
        form.param("user_id", userId);
        form.param("starts_at", "2016-04-03");
        form.param("ends_at", "2016-08-03");
        post("/projects/" + projectId + "/users", cookie, form);
    }

    public String getAssignedProjectName() {
        final Response response = get(String.format("/users/%s/projects", userId), cookie);
        List projects = response.readEntity(ArrayList.class);
        return (String) projects
                .stream()
                .map(project -> ((Map) project).get("name").toString())
                .collect(Collectors.joining(","));
    }
}
