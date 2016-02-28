package com.tw.mapper;

import com.tw.domain.Capability;
import com.tw.domain.TestHelper;
import com.tw.mapper.util.PrimaryKey;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class CapabilityMapperTest extends MapperTestBase {
    private long serviceId;
    private Map<String, Object> stackMap;
    private Map<String, Object> service;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        service = TestHelper.service(1L).toJson();
        serviceMapper.save(service);
        serviceId = (long) service.get("id");

        stackMap = new HashMap<>();
        stackMap.put("name", "stack");
        stackMap.put("description", "stack description");
        stackMap.put("services", asList(serviceId));
    }

    @Test
    public void should_create_and_get_capability() throws Exception {
        PrimaryKey stackPrimaryKey = new PrimaryKey();
        stackRepository.create(stackMap, stackPrimaryKey);

        PrimaryKey solutionPrimaryKey = new PrimaryKey();
        solutionRepository.create(TestHelper.solution(1L).toJson(), solutionPrimaryKey);

        Map<String, Object> map = new HashMap<>();
        map.put("solution_id", solutionPrimaryKey.id);
        map.put("stack_id", stackPrimaryKey.id);
        final int result = capabilityMapper.save(100L, map);

        assertThat(result, not(nullValue()));

        Capability capability = capabilityMapper.findById(100L, (long) map.get("id"));
        assertThat(capability.getId(), is(map.get("id")));
        assertThat(capability.getStack().getName(), is(stackMap.get("name")));
//        assertThat(capability.getStack().getServices().size(), is(1));
//        assertThat(capability.getStack().getServices().get(0).getName(), is(service.get("name")));
    }

    @Test
    public void should_get_a_list_of_capabilities_without_deprecated() throws Exception {
        final long capabilityId = createCapability(1L);
        Capability capability = capabilityMapper.findById(1L,capabilityId);
        capabilityMapper.deprecate(capability);

        createCapability(1L);

        assertThat(capabilityMapper.find(1L, false).size(), is(1));
    }

    @Test
    public void should_deprecate_capability() throws Exception {
        final long capabilityId = createCapability(1L);
        Capability capability = capabilityMapper.findById(1L, capabilityId);
        capabilityMapper.deprecate(capability);
        capability = capabilityMapper.findById(1L, capabilityId);
        assertThat(capability.getDeprecatedAt(), not(nullValue()));
    }

    @Test
    public void should_get_capabilities_including_deprecated() throws Exception {
        final long capabilityId = createCapability(1L);
        Capability capability = capabilityMapper.findById(1L, capabilityId);
        capabilityMapper.deprecate(capability);

        final long capabilityId2 = createCapability(1L);

        assertThat(capabilityMapper.find(1L, true).size(), is(2));
    }
}