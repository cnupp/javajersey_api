package specs.example;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import specs.model.Capability;
import specs.model.Member;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RunWith(ConcordionRunner.class)
public class StackOwnerTest extends ConcordionBaseTest {

    public String uniqueProjectId() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        return (instance.getTimeInMillis() + "").substring(4, 12);
    }

    public String userLogin(String username) {
        Form form = new Form();
        form.param("user_name", username);
        final Response response = post("/authentication", null, form);
        cookie = response.getHeaderString("Set-Cookie");
        return response.getStatus() + "";
    }

    public void setLoginUser(String username) {
        userLogin(username);
    }

    public String importNewProject(String id, String name, String account) {
        Form form = new Form();
        form.param("name", name);
        form.param("id", id);
        form.param("account", account);
        final Response response = post("/projects", cookie, form);

        return response.getStatus() + "";
    }

    public String getProjectName(String id) {
        final Response response = get("/projects/" + id, cookie);
        Map map = response.readEntity(Map.class);

        return map.get("name") + "";
    }

    public String getProjectAccount(String id) {
        final Response response = get("/projects/" + id, cookie);
        Map map = response.readEntity(Map.class);

        return map.get("account") + "";
    }

    public String defineANewCapability(String projectId, String solutionId, String stackId) {
        Form form = new Form();
        form.param("solution_id", solutionId);
        form.param("stack_id", stackId);
        final Response response = post("/projects/" + projectId + "/capabilities", cookie, form);
        return response.getStatus() + "";
    }

    public Iterable<Capability> getFirstCapability(String id) {
        final Response response = get("/projects/" + id + "/capabilities", cookie);
        List<Map> capabilities = response.readEntity(ArrayList.class);
        return capabilities.stream().map(capability -> {
            Map stack = (Map) capability.get("stack");
            Map solution = (Map) capability.get("solution");
            return new Capability(
                    capability.get("id") + "",
                    stack.get("id") + "",
                    stack.get("name") + "",
                    solution.get("id") + "",
                    solution.get("name") + ""
            );
        }).collect(toList());
    }

    public String getOneCapability(String id) {
        Iterator<Capability> iterator = getFirstCapability(id).iterator();
        return iterator.next().getId();
    }

    public String defineANewSolution(String solutionName) {
        Form form = new Form();
        form.param("name", solutionName);
        final Response response = post("/solutions", cookie, form);
        return response.getStatus() + "";
    }

    public String defineANewStack(String stackName) {
        Form form = new Form();
        form.param("name", stackName);
        form.param("services", "1");
        form.param("services", "2");
        final Response response = post("/stacks", cookie, form);
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

    public Iterable<Member> getAllMembers(String projectId) {
        final Response response = get("/projects/" + projectId + "/users", cookie);
        List<Map> members = response.readEntity(ArrayList.class);
        List<Member> collect = members.stream().map(member -> new Member(
                member.get("id") + "",
                member.get("name") + ""
        )).collect(toList());
        return removeDuplicateMembers(collect);
    }

    private List<Member> removeDuplicateMembers(List<Member> origin) {
        List<Member> members = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        origin.stream().forEach(member -> {
            if (keys.indexOf(member.getName()) == -1) {
                member.getName();
                keys.add(member.getName());
                members.add(member);
            }
        });
        return members;
    }

    public String deprecateStack(String projectId, String capabilityId) {
        final Response response = post("/projects/" + projectId + "/capabilities/" + capabilityId + "/deprecated", cookie, new Form());
        return response.getStatus() + "";
    }
}
