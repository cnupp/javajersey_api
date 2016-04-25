package com.tw.core;

import java.util.Map;

public interface CustomerRepository {
    Customer register(Map<String, Object> body);

    Customer findById(String id);

    Customer findByName(String userName);
}
