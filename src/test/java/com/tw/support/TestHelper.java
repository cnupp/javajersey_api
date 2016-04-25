package com.tw.support;

import javax.ws.rs.core.Form;
import java.util.HashMap;
import java.util.Map;

public class TestHelper {

    public static Map<String, Object> customer() {
        return new HashMap<String, Object>() {{
            put("username", "guest");
            put("email", "guest@test.com");
            put("telephone", "17233874784");
            put("password", "abel38d2dslsd=");
        }};
    }

    public static Form auth(String username) {
        Form form = new Form();
        form.param("user_name", username);
        return form;
    }
}
