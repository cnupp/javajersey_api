package com.tw.records;

import com.tw.core.Customer;
import com.tw.core.Record;
import com.tw.jersey.Routes;

import java.util.HashMap;
import java.util.Map;

public class CustomerRecord implements Customer, Record {
    private String id;
    private String username;
    private String telephone;
    private String email;
    private String password;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> toJson(Routes routes) {
        return new HashMap<String, Object>() {{
            put("id", id);
            put("username", username);
            put("email", email);
            put("telephone", telephone);
        }};
    }

    @Override
    public Map<String, Object> toRefJson(Routes routes) {
        return toJson(routes);
    }
}
