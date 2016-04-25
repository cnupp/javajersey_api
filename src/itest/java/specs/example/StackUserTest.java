package specs.example;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(ConcordionRunner.class)
public class StackUserTest extends ConcordionBaseTest{
    String userId="123123";
    String username="scxu";
    String email="scxu@thoughtworks.com";

    public String importUser() {
        Map<String, Object> userInfo = new HashMap<String, Object>() {{
            put("id", userId);
            put("email", email);
            put("name", username);
            put("role", "DEV");
        }};

        final Response response = post("/users", cookie, userInfo);
        return response.getStatus() + "";
    }

    private String importNewProject(String id, String name, String account) {

        Map<String, Object> projectMap = new HashMap<String, Object>() {{
            put("id", id);
            put("name", name);
            put("account", account);
        }};

        final Response response = post("/projects", cookie, projectMap);

        return response.getStatus() + "";
    }

    public void assignUserToProject(){
        final String projectId = "1024";
        importNewProject(projectId, "CMS", "ThoughtWorks");

        Map<String, Object> param = new HashMap<String, Object>() {{
            put("user", userId);
        }};

        post("/projects/" + projectId + "/users", cookie, param);
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
