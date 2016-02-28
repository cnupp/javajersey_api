package com.tw.mapper;

import com.tw.domain.Capability;
import com.tw.domain.Qualification;
import com.tw.domain.TestHelper;
import com.tw.mapper.util.PrimaryKey;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class QualificationMapperTest extends MapperTestBase {

    private long projectId = 100L;
    private long userId = 100L;
    private Map<String, Object> solution1;
    private Map<String, Object> solution2;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        solution1 = TestHelper.solution(1L).toJson();
        solution2 = TestHelper.solution(2L).toJson();
    }

    @Test
    public void should_create_qualification() throws Exception {
        Map<String, Object> stack = TestHelper.stackToMap(TestHelper.stack(1L));
        PrimaryKey stackPrimaryKey = new PrimaryKey();
        stackRepository.create(stack, stackPrimaryKey);

        PrimaryKey solutionPrimaryKey = new PrimaryKey();
        solutionRepository.create(solution1, solutionPrimaryKey);

        Map<String, Object> map = new HashMap<>();
        map.put("solution_id", solutionPrimaryKey.id);
        map.put("stack_id", stackPrimaryKey.id);
        capabilityMapper.save(projectId, map);
        Capability capability = capabilityMapper.findById(projectId, (long) map.get("id"));

        Map<String, Object> qualificationMap = qualificationMap(capability.getId());

        qualificationMapper.save(userId, qualificationMap);
        assertThat(qualificationMap.get("id"), not(nullValue()));
    }

    @Test
    public void should_find_all_qualifications_belonged_to_user() throws Exception {
        long capability1 = createCapability(projectId);
        Map<String, Object> qualificationMap1 = qualificationMap(capability1);

        long capability2 = createCapability(projectId);
        Map<String, Object> qualificationMap2 = qualificationMap(capability2);

        qualificationMapper.save(userId, qualificationMap1);
        qualificationMapper.save(userId, qualificationMap2);
        long userId2 = 200L;
        qualificationMapper.save(userId2, qualificationMap2);

        List<Qualification> qualificationsBelongedToUser1 = qualificationMapper.findQualificationsBelongedToUser(userId);
        assertThat(qualificationsBelongedToUser1.size(), is(2));
    }

    @Test
    public void should_get_user_qualifications_of_one_project() throws Exception {
        long capability1 = createCapability(projectId);
        Map<String, Object> qualificationMap1 = qualificationMap(capability1);

        long capability2 = createCapability(101L);
        Map<String, Object> qualificationMap2 = qualificationMap(capability2);

        qualificationMapper.save(userId, qualificationMap1);
        qualificationMapper.save(userId, qualificationMap2);

        long userId2 = 200L;
        qualificationMapper.save(userId2, qualificationMap2);

        List<Qualification> qualificationsBelongedToUser1 = qualificationMapper.findQualificationsBelongToUserOfProject(projectId, userId);
        assertThat(qualificationsBelongedToUser1.size(), is(1));
    }

    private Map<String, Object> qualificationMap(long capabilityId) {
        Map<String, Object> qualificationMap = new HashMap<>();
        qualificationMap.put("capability_id", capabilityId);
        qualificationMap.put("score", 100);
        return qualificationMap;
    }


}