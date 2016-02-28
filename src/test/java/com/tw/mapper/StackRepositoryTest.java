package com.tw.mapper;

import com.tw.domain.Service;
import com.tw.domain.Stack;
import com.tw.domain.TestHelper;
import com.tw.mapper.util.PrimaryKey;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class StackRepositoryTest extends MapperTestBase{
    private long serviceId;
    private Map<String, Object> stackMap;
    private Map<String, Object> service;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        service = TestHelper.service(1L).toJson();
        serviceMapper.save(service);
        serviceId = (long) service.get("id");

        PrimaryKey primaryKey = new PrimaryKey();
        stackMap = new HashMap<>();
        stackMap.put("name", "stack");
        stackMap.put("description", "stack description");
        stackMap.put("services", asList(serviceId));
    }

    @Test
    public void should_create_new_stack() throws Exception {
        Map<String, Object> expected = new HashMap<>();
        expected.put("name", "stack");
        expected.put("description", "stack description");
        expected.put("services", asList(1L, 2L));

        PrimaryKey primaryKey = new PrimaryKey();
        stackRepository.create(expected, primaryKey);
        assertThat(primaryKey, is(notNullValue()));
    }

    @Test
    public void should_get_all_stacks() throws Exception {
        stackRepository.create(stackMap, new PrimaryKey());
        stackRepository.create(stackMap, new PrimaryKey());

        List<Stack> stacks = stackRepository.getStacks();

        assertThat(stacks.size(), is(2));
        System.out.println(stacks);
        assertThat(stacks.get(0).getName(), is(stackMap.get("name")));
        assertThat(stacks.get(1).getName(), is(stackMap.get("name")));

    }

    @Test
    public void should_get_stack_by_id() throws Exception {
        PrimaryKey primaryKey = new PrimaryKey();
        stackRepository.create(stackMap, primaryKey);
        Stack stack = stackRepository.getStackById(primaryKey.id);

        assertThat(stack.getName(), is(stackMap.get("name")));
        assertThat(stack.getDescription(), is(stackMap.get("description")));
        assertThat(stack.getServices().size(), is(1));
        Service service = stack.getServices().get(0);
        assertThat(service.getName(), is(service.getName()));

    }
}
