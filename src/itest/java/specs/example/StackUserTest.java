package specs.example;

import specs.model.Qualification;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class StackUserTest extends ConcordionBaseTest{

    public String userLogin(String username) {
        Form form = new Form();
        form.param("user_name", username);
        final Response response = post("/authentication", null, form);
        cookie = response.getHeaderString("Set-Cookie");
        return response.getStatus() + "";
    }

    public String userLogout() {
        final Response response = delete("/authentication", null);
        return response.getStatus() + "";
    }

    public void setLoginUser(String username) {
        userLogin(username);
    }

    public String getCurrentUser() {
        final Response response = get("/users/current", cookie);
        return response.readEntity(Map.class).get("role") + "";
    }

    public String createUser(String userId, String username) {
        Form form = new Form();
        form.param("name", username);
        form.param("id", userId);
        final Response response = post("/users", cookie, form);
        return response.getStatus() + "";
    }

    public String uniqueUserId() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        return instance.getTimeInMillis()+"";
    }

    public String getProjects() {
        final Response response = get("/users/22222/projects", cookie);
        return response.getStatus() + "";
    }

    public String getProjectsSize() {
        final Response response = get("/users/22222/projects", cookie);
        List map = response.readEntity(ArrayList.class);
        return map.size() + "";
    }
    public Iterable<String> getSearchResultsFor(){
        final Response response = get("/users/22222/projects", cookie);
        List<Map> projects = response.readEntity(ArrayList.class);
        return projects.stream().map(project ->
                        project.get("name").toString()
        ).collect(toList());
    }
    public String getCapabilityNumber(){
        final Response response = get("/users/22222/qualifications", cookie);
        List<Map> projects = response.readEntity(ArrayList.class);
        return projects.size()+"";
    }
    public Iterable<Qualification> getQualifications() {
        final Response response = get("/users/22222/qualifications", cookie);
        List<Map> projects = response.readEntity(ArrayList.class);
        return projects.stream().limit(2).map(project ->
                        new Qualification(project.get("solution_name") + "", project.get("stack_name")+"")
        ).collect(toList());
    }
}
