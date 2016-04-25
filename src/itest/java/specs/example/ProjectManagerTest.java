package specs.example;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(ConcordionRunner.class)
public class ProjectManagerTest extends ConcordionBaseTest {

    public String importUser(String userId, String username, String email) {
        Map<String, Object> userInfo = new HashMap<String, Object>() {{
            put("id", userId);
            put("email", email);
            put("name", username);
            put("role", "DEV");
        }};
        final Response response = post("/users", cookie, userInfo);
        return response.getStatus() + "";
    }

    public String uniqueUserId() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        return (instance.getTimeInMillis() + "").substring(4, 12);
    }

    public String uniqueProjectId() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        return (instance.getTimeInMillis() + "").substring(4, 12);
    }

    public String importNewProject(String id, String name, String account) {
        Map<String, Object> projectMap = new HashMap<String, Object>() {{
            put("id", id);
            put("name", name);
            put("account", account);
        }};

        final Response response = post("/projects", cookie, projectMap);

        return response.getStatus() + "";
    }

    public String addNewMember(String userId, String start, String end, String projectId) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("user", userId);
        }};

        final Response response = post("/projects/" + projectId + "/members", cookie, param);
        return response.getStatus() + "";
    }

    public void setLoginUser(String username) {

    }
}
