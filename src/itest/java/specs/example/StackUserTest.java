package specs.example;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import specs.model.User;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(ConcordionRunner.class)
public class StackUserTest extends ConcordionBaseTest{
    String username="scxu";
    String email="scxu@thoughtworks.com";

    public String uniqueUserId() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        return (instance.getTimeInMillis() + "").substring(4, 12);
    }

    public String importUser(String userId) {
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

    public void assignUserToProject(String userId){
        final String projectId = uniqueProjectId();
        importNewProject(projectId, "CMS", "ThoughtWorks");

        Map<String, Object> param = new HashMap<String, Object>() {{
            put("user", userId);
        }};

        post("/projects/" + projectId + "/members", cookie, param);
    }

    private String uniqueProjectId() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        return (instance.getTimeInMillis() + "").substring(4, 12);
    }

    public String getAssignedProjectName(String userId) {
        final Response response = get(String.format("/users/%s/projects", userId), cookie);
        System.out.println(response.getStatus());
        List projects = response.readEntity(ArrayList.class);
        return (String) projects
                .stream()
                .map(project -> ((Map) project).get("name").toString())
                .collect(Collectors.joining(","));
    }

    public User getProfile(String userId) {
        final Response response = get(String.format("/users/%s", userId), cookie);
        final Map map = response.readEntity(Map.class);
        return new User(map.get("name")+"", map.get("id")+"");
    }
}
