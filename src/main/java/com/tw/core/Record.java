package com.tw.core;

import com.tw.jersey.Routes;

import java.util.Map;

public interface Record {
    Map<String, Object> toJson(Routes routes);
    Map<String, Object> toRefJson(Routes routes);

}
