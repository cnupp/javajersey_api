package com.tw.mapper;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class ServiceMapperTest extends MapperTestBase {
    @Test
    public void should_create_service() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "name");
        map.put("imageUrl", "url");

        serviceMapper.save(map);
        assertThat(map.get("id"), not(nullValue()));
    }
}