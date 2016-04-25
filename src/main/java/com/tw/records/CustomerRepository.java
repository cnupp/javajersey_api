package com.tw.records;

import com.tw.core.Customer;
import com.tw.mappers.CustomerMapper;
import com.tw.resources.exception.InvalidParameterException;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

public class CustomerRepository implements com.tw.core.CustomerRepository {
    @Inject
    CustomerMapper mapper;

    @Override
    public Customer register(Map<String, Object> body) {
        final Customer customer = mapper.findByName(body.get("username") + "");
        if (customer != null){
            throw new InvalidParameterException("用户名已存在");
        }

        final String id = UUID.randomUUID().toString();
        body.put("id", id);
        mapper.save(body);
        return mapper.findById(id);
    }

    @Override
    public Customer findById(String id) {
        return mapper.findById(id);
    }

    @Override
    public Customer findByName(String userName) {
        return mapper.findByName(userName);
    }
}
