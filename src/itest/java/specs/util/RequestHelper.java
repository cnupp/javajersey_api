package specs.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

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

    public Response post(String uri, String cookie, Form form) {
        return client.target(getUrl(uri))
                .request()
                .header("Cookie", cookie)
                .post(Entity.form(form));
    }

    public String login() {
        Form form = new Form();
        form.param("user_name", "bg");
        final Response response = post("/authentication", null, form);
        return response.getHeaderString("Set-Cookie");
    }

    public boolean submitResult(String resultUrl, String cookie, Form form) {
        int status = post(resultUrl, cookie, form).getStatus();
        System.out.println("Submit Response Status Code is" + status);
        return status == 200;
    }
}
