package specs.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class RequestHelper {
    Client client;
    private final String entryPoint;

    public RequestHelper(String entryPoint) {
        this.entryPoint = entryPoint;
        client = ClientBuilder.newClient();
    }

    public String getUrl(String path) {
        return path.startsWith("http") ? path : (entryPoint + path);
    }

    public String login() {
        Map<String, Object> info = new HashMap<>();
        info.put("user_name", "admin");
        final Response response = client.target(getUrl("/authentication"))
                .request()
                .header("Cookie", "")
                .header("Content-Type", "application/json")
                .post(Entity.json(info));
        return response.getHeaderString("Set-Cookie");
    }

    public boolean submitResult(String resultUrl, String cookie, Map<String, Object> form) {
        int status = client.target(getUrl(resultUrl))
                .request()
                .header("Cookie", cookie)
                .header("Content-Type", "application/json")
                .put(Entity.json(form)).getStatus();
        System.out.println("Submit Response Status Code is" + status);
        return status == 200;
    }
}
